import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../api/authService";
import { useAuth } from "../context/AuthContext";
import LoginView from "../components/login/LoginView";
import { AxiosError } from "axios";
import type { ApiErrorResponse } from "../types/dto/ApiErrorResponse";

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    const errors: string[] = [];

    if (!username.trim()) {
      errors.push("Username cannot be empty");
    }

    if (!password.trim()) {
      errors.push("Password cannot be empty");
    }

    if (errors.length > 0) {
      setError(errors.join(", "));
      return;
    }

    setLoading(true);

    try {
      const res = await loginUser({ username, password });
      login(res.token);
      navigate("/");
    } catch (err) {
      const error = err as AxiosError<ApiErrorResponse>;
      const data = error.response?.data;

      setError(data?.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <LoginView
      username={username}
      password={password}
      error={error}
      loading={loading}
      setUsername={setUsername}
      setPassword={setPassword}
      handleSubmit={handleSubmit}
    />
  );
}
