import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

const API = axios.create({
  baseURL: API_BASE_URL,
});

API.interceptors.request.use((req) => {
  const token = localStorage.getItem("token");

  if (token) {
    if (!req.headers) {
      req.headers = {};
    }
    req.headers.Authorization = `Bearer ${token}`;
    console.log("Sending request to:", req.url);
    console.log("Token:", token.substring(0, 50) + "...");
  } else {
    console.log("No token found for request to:", req.url);
  }

  return req;
});

API.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 403) {
      console.error("403 Forbidden - Authorization check failed");
      console.error("Response data:", error.response?.data);
      console.error("Request headers:", error.config?.headers);
    }
    return Promise.reject(error);
  }
);

export default API;