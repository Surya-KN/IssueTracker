
# Issue Tracker

This is a simple issue tracker application. It consists of a frontend built with react, backend built with Spring Boot and detects duplicate tickets using cosine similarity and scentence embeddings.

![](https://app.eraser.io/workspace/qaqSO5QauKiSdVBXIyIt/preview?elements=Zs6tVZdpC96ByCCAIYpChQ&type=embed)

## Getting Started

To get started with this project, you need to have Docker and Docker Compose installed on your machine.

1. Clone the repository:

```bash
git clone https://github.com/Surya-KN/IssueTracker
cd IssueTracker
```

2. Build and start the Docker containers:

```bash
docker-compose up --build
```

The frontend will be available at `http://localhost:5173` and the backend will be available at `http://localhost:6969`.

## Development

To start the frontend in development mode, navigate to the [``issue-tracker-web``](https://github.com/Surya-KN/IssueTracker/blob/main/issue-tracker-web) directory and run:

```bash
npm install
npm run dev
```

To start the backend in development mode, you can use the provided Maven wrapper:

```bash
./mvnw spring-boot:run
```
To start the duplication service in development mode, navigate to the [``duplicate-detection``](https://github.com/Surya-KN/IssueTracker/blob/main/duplicate-detection) directory and run:

```bash
python .\dup.py 
```
before that recomended to use virtual environment for python
```
python -m venv venv
.\venv\Scripts\activate
pip install -r requirements.txt
```
> **Note:** change db links in compose file or in application.properties file. Spring jpa will create tables automatically.