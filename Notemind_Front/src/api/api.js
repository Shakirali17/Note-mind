import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

export const loginUser = (data) => {
  return api.post("/users/login", data);
};

export const registerUser = (data) => {
  return api.post("/users/register", data);
};

export const getUserNotes = (userId) => {
  return api.get(`/notes/user/${userId}`);
};

export const getNoteById = (noteId) => {
  return api.get(`/notes/${noteId}`);
};

export const uploadVoice = (formData) => {
  return api.post("/voice/upload", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};

export const summarizeNote = (noteId) => {
  return api.post(`/voice/summarize/${noteId}`);
};
