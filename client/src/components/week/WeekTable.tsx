import type { FullHabitLogsForWeek } from "../../types/dto/response/FullHabitLogsForWeek";
import WeekHabitRow from "./WeekHabitRow";

import "../../styles/weeks/WeekTable.css";

interface WeekTableProps {
  habits: FullHabitLogsForWeek[];

  dates: Date[];

  onDailyMinutesChange: (
    habitId: number,
    date: string,
    minutes: number
  ) => void;

  onWeeklyGoalChange: (habitId: number, goal: number) => void;
}

export default function WeekTable({
  habits,
  dates,
  onDailyMinutesChange,
  onWeeklyGoalChange,
}: WeekTableProps) {
  const today = new Date();

  today.setHours(0, 0, 0, 0);

  return (
    <div className="week-table-container">
      <table className="week-table">
        <thead>
          <tr>
            <th className="habit-column">Habit</th>

            <th className="goal-column">Goal</th>

            {dates.map((date) => {
              const headerDate = new Date(date);

              headerDate.setHours(0, 0, 0, 0);

              const isToday = headerDate.getTime() === today.getTime();

              return (
                <th
                  key={date.toISOString()}
                  className={`day-column ${
                    isToday ? "current-day-column" : ""
                  }`}
                >
                  {date.toLocaleDateString("en-US", {
                    weekday: "short",
                  })}
                </th>
              );
            })}

            <th className="total-column">Total</th>

            <th className="imbalance-column">Imbalance</th>
          </tr>
        </thead>

        <tbody>
          {habits.map((habitData) => (
            <WeekHabitRow
              key={habitData.habit.id}
              habitData={habitData}
              dates={dates}
              onDailyMinutesChange={onDailyMinutesChange}
              onWeeklyGoalChange={onWeeklyGoalChange}
            />
          ))}
        </tbody>
      </table>
    </div>
  );
}
