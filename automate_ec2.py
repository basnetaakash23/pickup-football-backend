import subprocess
import sys

# === CONFIG ===
aws_region = "us-east-1"
ecr_repo = "**********.dkr.ecr.us-east-1.amazonaws.com/pickup-football-app:latest"
image_name = "pickup-football"
tag = ":latest"

def run_cmd(cmd, check=True):
    print(f"\n▶ Running: {cmd}")
    result = subprocess.run(cmd, shell=True, check=check, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    if result.returncode == 0:
        print(result.stdout.decode())
    else:
        print(result.stderr.decode(), file=sys.stderr)
    return result

try:
    # 1. Build Spring Boot JAR
    run_cmd("./mvnw clean package -DskipTests")

    # 2. Authenticate Docker to ECR
    login_cmd = (
        f"aws ecr get-login-password --region {aws_region} | "
        f"docker login --username AWS --password-stdin {ecr_repo.split('/')[0]}"
    )
    run_cmd(login_cmd)

    # 3. Build Docker Image
    run_cmd(f"docker buildx build --platform linux/amd64 -t {image_name+tag} --load .")

    # 4. Tag Docker Image
    run_cmd(f"docker tag {image_name+tag} {ecr_repo}")

    # 5. Push to ECR
    run_cmd(f"docker push {ecr_repo}")

    print(f"✅ Successfully pushed image to {ecr_repo}")

except subprocess.CalledProcessError as e:
    print(f"❌ Error: {e}", file=sys.stderr)
