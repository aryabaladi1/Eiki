import type { HabitResponse } from "../../types/dto/response/HabitResponse";
import {
  formatHabitTime,
  formatRelativeDate,
  type TimeDisplayMode,
} from "../../utils/time";

import { useState } from "react";
import { MoreVertical, Pencil, Archive, ArchiveRestore } from "lucide-react";

import "../../styles/habit/HabitCard.css";
import { capitalizeFirstLetter } from "../../utils/text";

interface HabitCardProps {
  habit: HabitResponse;

  menuOpen: boolean;

  onMenuToggle: (id: number) => void;

  onMenuClose: () => void;

  onEdit?: (habit: HabitResponse) => void;
  onArchive?: (habit: HabitResponse) => void;
  onUnarchive?: (habit: HabitResponse) => void;
}

export default function HabitCard({
  habit,
  menuOpen,
  onMenuToggle,
  onMenuClose,
  onEdit,
  onArchive,
  onUnarchive,
}: HabitCardProps) {
  const [timeMode, setTimeMode] = useState<TimeDisplayMode>("standard");

  return (
    <div className="habit-card" onMouseLeave={onMenuClose}>
      <div className="habit-card-top">
        <h2 title={capitalizeFirstLetter(habit.name)}>
          {capitalizeFirstLetter(habit.name)}
        </h2>

        <div className="habit-menu-container">
          <button
            className="icon-button"
            onClick={(e) => {
              e.stopPropagation();
              onMenuToggle(habit.id);
            }}
          >
            <MoreVertical size={18} />
          </button>

          {menuOpen && (
            <div className="habit-menu">
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  onMenuClose();
                  onEdit?.(habit);
                }}
              >
                <Pencil size={16} />
                Edit
              </button>

              {!habit.archived ? (
                <button
                  className="danger"
                  onClick={(e) => {
                    e.stopPropagation();
                    onMenuClose();
                    onArchive?.(habit);
                  }}
                >
                  <Archive size={16} />
                  Archive
                </button>
              ) : (
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    onMenuClose();
                    onUnarchive?.(habit);
                  }}
                >
                  <ArchiveRestore size={16} />
                  Unarchive
                </button>
              )}
            </div>
          )}
        </div>
      </div>

      <div className="habit-card-content">
        <p
          className="habit-description"
          title={habit.description || "No description."}
        >
          {habit.description || "No description."}
        </p>
      </div>

      <div className="habit-footer">
        <div className="habit-meta">{formatRelativeDate(habit.createdAt)}</div>

        <div
          className="habit-time"
          onClick={() => {
            setTimeMode((prev) => {
              if (prev === "standard") return "hours";
              if (prev === "hours") return "minutes";
              return "standard";
            });
          }}
        >
          {formatHabitTime(habit.minutesTotal, timeMode)}
        </div>
      </div>
    </div>
  );
}
