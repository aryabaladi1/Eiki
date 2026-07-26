import { useEffect, useState } from "react";

import {
  createHabit,
  getAllHabits,
  updateHabit,
  archiveHabit,
  unarchiveHabit,
} from "../api/habitService";

import type { HabitResponse } from "../types/dto/response/HabitResponse";
import { AxiosError } from "axios";

import HabitCard from "../components/habit/HabitCard";

import type { ApiErrorResponse } from "../types/dto/ApiErrorResponse";

import { ChevronDown } from "lucide-react";

import "../styles/habit/HabitsPage.css";
import { HabitCreateRequest } from "../types/dto/request/HabitCreateRequest";
import HabitModal from "../components/habit/HabitModal";
import ConfirmationModal from "../components/common/ConfirmationModal";

export default function HabitsPage() {
  const [habits, setHabits] = useState<HabitResponse[]>([]);

  const [showModal, setShowModal] = useState(false);
  const [editingHabit, setEditingHabit] = useState<HabitResponse | null>(null);

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const [error, setError] = useState("");
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});

  const [openMenuId, setOpenMenuId] = useState<number | null>(null);
  const [archiveCandidate, setArchiveCandidate] =
    useState<HabitResponse | null>(null);

  const [showArchived, setShowArchived] = useState(false);

  useEffect(() => {
    fetchHabits();
  }, []);

  async function fetchHabits() {
    try {
      const data = await getAllHabits();
      setHabits(data);
    } catch (err) {
      console.error(err);
      setError("Failed to load habits.");
    } finally {
      setLoading(false);
    }
  }

  async function handleSaveHabit(request: HabitCreateRequest) {
    setError("");
    setFieldErrors({});

    try {
      setSaving(true);

      if (editingHabit) {
        const updated = await updateHabit(editingHabit.id, request);

        setHabits((prev) =>
          prev.map((h) => (h.id === updated.id ? updated : h))
        );
      } else {
        const created = await createHabit(request);

        setHabits((prev) => [created, ...prev]);
      }

      setShowModal(false);
      setEditingHabit(null);
    } catch (err) {
      const error = err as AxiosError<ApiErrorResponse>;

      const data = error.response?.data;

      if (!data) {
        setError("Failed to save habit.");
        return;
      }

      if (data.errors && Object.keys(data.errors).length > 0) {
        setFieldErrors(data.errors);
      } else {
        setError(data.message);
      }
    } finally {
      setSaving(false);
    }
  }

  async function handleArchiveHabit(habit: HabitResponse) {
    try {
      const archived = await archiveHabit(habit.id);

      setHabits((prev) =>
        prev.map((h) => (h.id === archived.id ? archived : h))
      );
    } catch {
      setError("Failed to archive habit.");
    }
  }

  async function handleUnarchiveHabit(habit: HabitResponse) {
    try {
      const updated = await unarchiveHabit(habit.id);

      setHabits((prev) => prev.map((h) => (h.id === updated.id ? updated : h)));
    } catch {
      setError("Failed to unarchive habit.");
    }
  }

  function handleMenuToggle(id: number) {
    setOpenMenuId((prev) => (prev === id ? null : id));
  }

  function handleMenuClose() {
    setOpenMenuId(null);
  }

  const activeHabits = habits.filter((habit) => !habit.archived);

  const archivedHabits = habits.filter((habit) => habit.archived);

  return (
    <div className="habits-container">
      <div className="habits-content">
        <div className="habits-header">
          <div>
            <h1>Your Habits</h1>

            <p>Build consistency through weekly progress.</p>
          </div>

          <button
            className="add-habit-button"
            onClick={() => {
              setEditingHabit(null);

              setFieldErrors({});
              setError("");

              setShowModal(true);
            }}
          >
            +
          </button>
        </div>

        <HabitModal
          open={showModal}
          habit={editingHabit ?? undefined}
          saving={saving}
          error={error}
          fieldErrors={fieldErrors}
          onClose={() => {
            setShowModal(false);
            setEditingHabit(null);
            setError("");
            setFieldErrors({});
          }}
          onSave={handleSaveHabit}
        />

        <ConfirmationModal
          open={archiveCandidate !== null}
          title="Archive Habit"
          message={`Are you sure you want to archive "${
            archiveCandidate?.name ?? ""
          }"?`}
          confirmText="Archive"
          danger
          onClose={() => setArchiveCandidate(null)}
          onConfirm={async () => {
            if (!archiveCandidate) return;

            await handleArchiveHabit(archiveCandidate);

            setArchiveCandidate(null);
          }}
        />

        {loading ? (
          <p className="habits-loading">Loading habits...</p>
        ) : habits.length === 0 ? (
          <p className="habits-empty">No habits yet.</p>
        ) : (
          <>
            {activeHabits.length > 0 && (
              <>
                <h2 className="habit-section-title">
                  Active Habits ({activeHabits.length})
                </h2>

                <div className="habits-grid">
                  {activeHabits.map((habit) => (
                    <HabitCard
                      key={habit.id}
                      habit={habit}
                      menuOpen={openMenuId === habit.id}
                      onMenuToggle={handleMenuToggle}
                      onMenuClose={handleMenuClose}
                      onEdit={(habit) => {
                        setEditingHabit(habit);

                        setFieldErrors({});
                        setError("");

                        setShowModal(true);

                        handleMenuClose();
                      }}
                      onArchive={(habit) => {
                        setArchiveCandidate(habit);
                        handleMenuClose();
                      }}
                      onUnarchive={handleUnarchiveHabit}
                    />
                  ))}
                </div>
              </>
            )}

            {archivedHabits.length > 0 && (
              <>
                <h2
                  className="habit-section-title archived-title clickable"
                  onClick={() => setShowArchived((prev) => !prev)}
                >
                  Archived Habits ({archivedHabits.length})
                  <span className="section-arrow">
                    <ChevronDown
                      size={18}
                      className={`section-arrow ${showArchived ? "open" : ""}`}
                    />
                  </span>
                </h2>

                {showArchived && (
                  <div className="habits-grid">
                    {archivedHabits.map((habit) => (
                      <HabitCard
                        key={habit.id}
                        habit={habit}
                        menuOpen={openMenuId === habit.id}
                        onMenuToggle={handleMenuToggle}
                        onMenuClose={handleMenuClose}
                        onEdit={(habit) => {
                          setEditingHabit(habit);

                          setFieldErrors({});
                          setError("");

                          setShowModal(true);

                          handleMenuClose();
                        }}
                        onArchive={handleArchiveHabit}
                        onUnarchive={handleUnarchiveHabit}
                      />
                    ))}
                  </div>
                )}
              </>
            )}
          </>
        )}
      </div>
    </div>
  );
}
