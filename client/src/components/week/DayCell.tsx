import type { DailyHabitLogResponse } from "../../types/dto/response/DailyHabitLogResponse";
import EditableNumberCell from "./EditableNumberCell";

import "../../styles/weeks/DayCell.css";

interface DayCellProps {
  habitId: number;

  date: string;

  dailyLog?: DailyHabitLogResponse;

  dailyGoal?: number | null;

  disabled?: boolean;

  onMinutesChange: (habitId: number, date: string, minutes: number) => void;
}

export default function DayCell({
  habitId,
  date,
  dailyLog,
  dailyGoal,
  disabled = false,
  onMinutesChange,
}: DayCellProps) {
  const minutes = dailyLog?.minutesDone ?? 0;

  function getProgressClass() {
    if (!dailyGoal || dailyGoal <= 0) {
      return "";
    }

    const ratio = minutes / dailyGoal;

    if (ratio === 0) return "progress-0";
    if (ratio < 0.5) return "progress-1";
    if (ratio < 1) return "progress-2";
    if (ratio < 1.5) return "progress-3";

    return "progress-4";
  }

  return (
    <td className={`day-cell ${disabled ? "archived-cell" : ""}`}>
      <div
        className={getProgressClass()}
        title={disabled ? "Archived habits cannot be edited" : ""}
      >
        <EditableNumberCell
          mode="time"
          value={dailyLog?.minutesDone}
          disabled={disabled}
          onSave={(minutes) => onMinutesChange(habitId, date, minutes)}
        />
      </div>
    </td>
  );
}
