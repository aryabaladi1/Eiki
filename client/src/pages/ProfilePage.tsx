import { useEffect, useState } from "react";
import { getUserDetails, updateUserDetails } from "../api/userService";
import { AxiosError } from "axios";
import type { ApiErrorResponse } from "../types/dto/ApiErrorResponse";
import type { UserResponse } from "../types/dto/response/UserResponse";

import "../styles/profile/ProfilePage.css";

export default function ProfilePage() {
  const [user, setUser] = useState<UserResponse | null>(null);

  const [username, setUsername] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    async function fetchUser() {
      try {
        const data = await getUserDetails();

        setUser(data);

        setUsername(data.username ?? "");
        setFirstName(data.firstName ?? "");
        setLastName(data.lastName ?? "");
      } catch (err) {
        console.error(err);
        setError("Failed to load user data.");
      } finally {
        setLoading(false);
      }
    }

    fetchUser();
  }, []);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();

    setError("");
    setSuccess("");

    if (!username.trim()) {
      setError("Username cannot be empty.");
      return;
    }

    setSaving(true);

    try {
      const updatedUser = await updateUserDetails({
        username,
        firstName,
        lastName,
      });

      setUser(updatedUser);

      setSuccess("Profile updated successfully.");
    } catch (err) {
      const error = err as AxiosError<ApiErrorResponse>;
      const data = error.response?.data;

      if (!data) {
        setError("Failed to update profile.");
        return;
      }

      if (data.errors && Object.keys(data.errors).length > 0) {
        setError(Object.values(data.errors).join(", "));
      } else {
        setError(data.message);
      }
    } finally {
      setSaving(false);
    }
  }

  if (loading) {
    return <div className="profile-loading">Loading profile...</div>;
  }

  return (
    <div className="profile-container">
      <div className="profile-content">
        <header className="profile-header">
          <h1>Profile</h1>
          <p>Manage your account information.</p>
        </header>

        <form onSubmit={handleSubmit} className="profile-form">
          <section className="profile-section">
            <h2>Personal Information</h2>

            <div className="profile-field">
              <label>Username</label>

              <input
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>

            <div className="profile-field">
              <label>First Name</label>

              <input
                type="text"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
              />
            </div>

            <div className="profile-field">
              <label>Last Name</label>

              <input
                type="text"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
              />
            </div>
          </section>

          <section className="profile-section">
            <h2>Account Information</h2>

            <div className="profile-meta-row">
              <span>Created</span>

              <span>
                {new Date(user!.createdAt).toLocaleDateString(undefined, {
                  year: "numeric",
                  month: "long",
                  day: "numeric",
                })}
              </span>
            </div>

            <div className="profile-meta-row">
              <span>Last Login</span>

              <span>
                {new Date(user!.lastLogin).toLocaleString(undefined, {
                  dateStyle: "long",
                  timeStyle: "short",
                })}
              </span>
            </div>
          </section>

          <button
            type="submit"
            disabled={saving}
            className="profile-save-button"
          >
            {saving ? "Saving..." : "Save Changes"}
          </button>

          {success && <p className="profile-success">{success}</p>}

          {error && <p className="profile-error">{error}</p>}
        </form>
      </div>
    </div>
  );
}
