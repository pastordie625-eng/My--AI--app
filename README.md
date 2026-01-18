# My AI App — MVP

Overview
- Android app (Kotlin + Jetpack Compose) that sends user messages to a backend.
- Backend (FastAPI) proxies to OpenAI chat completions. Keep the API key in an environment variable.

Local backend run (dev)
1. Set env var:
   - Linux/macOS: `export OPENAI_API_KEY=\"sk-...\"`
   - Windows (PowerShell): `$env:OPENAI_API_KEY=\"sk-...\"`
2. Run with uvicorn:
   - `cd backend`
   - `pip install -r requirements.txt`
   - `uvicorn main:app --reload --port 8080`
3. For Android emulator use `http://10.0.2.2:8080/` as backend base URL. For a physical device, use your machine IP.

Docker / Cloud Run
- Build: `docker build -t myai-backend ./backend`
- Run: `docker run -e OPENAI_API_KEY=\"sk-...\" -p 8080:8080 myai-backend`

Android
- In `MainActivity.kt` set `backendUrl` to the backend base URL.
- Run on emulator. For local backend use `http://10.0.2.2:8080/`.

Security
- NEVER embed the provider API key in the Android app.
- Use the backend to enforce rate limiting, auth, and usage logging.

Next improvements
- Add authentication (Firebase Auth).
- Add embeddings + vector DB for retrieval (Pinecone, Redis, Weaviate).
- Add streaming responses (SSE / streaming LLM responses) and message persistence (Room).