from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import os
import httpx

app = FastAPI()

OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"

class ChatRequest(BaseModel):
    message: str

class ChatResponse(BaseModel):
    reply: str

@app.post("/chat", response_model=ChatResponse)
async def chat(req: ChatRequest):
    if not OPENAI_API_KEY:
        raise HTTPException(status_code=500, detail="Server missing OPENAI_API_KEY")
    headers = {
        "Authorization": f"Bearer {OPENAI_API_KEY}",
        "Content-Type": "application/json"
    }
    payload = {
        "model": "gpt-3.5-turbo",
        "messages": [{"role": "user", "content": req.message}],
        "max_tokens": 512,
        "temperature": 0.7
    }
    async with httpx.AsyncClient(timeout=30.0) as client:
        r = await client.post(OPENAI_API_URL, json=payload, headers=headers)
    if r.status_code != 200:
        raise HTTPException(status_code=502, detail=f"Upstream error: {r.text}")
    data = r.json()
    try:
        reply = data["choices"][0]["message"]["content"]
    except Exception:
        raise HTTPException(status_code=502, detail="Malformed upstream response")
    return ChatResponse(reply=reply.strip())
