import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../api/authService";
import { useAuth } from "../context/AuthContext";
import RegisterView from "../components/register/RegisterView";
import { AxiosError } from "axios";
import type { ApiErrorResponse } from "../types/dto/ApiErrorResponse";

export default function RegisterPage() {
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
      const res = await registerUser({ username, password });
      login(res.token);
      navigate("/");
    } catch (err) {
      const error = err as AxiosError<ApiErrorResponse>;
      const data = error.response?.data;

      if (!data) {
        setError("Registration failed");
        return;
      }

      if (data.errors && Object.keys(data.errors).length > 0) {
        setError(Object.values(data.errors).join(", "));
      } else {
        setError(data.message);
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <RegisterView
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
