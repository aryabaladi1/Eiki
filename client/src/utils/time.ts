export function minutesToTime(
  minutes: number,
  options?: {
    includeSign?: boolean;
  }
): string {
  const includeSign = options?.includeSign ?? false;

  const sign = minutes > 0 ? "+" : minutes < 0 ? "-" : "";
  const absolute = Math.abs(minutes);

  const hours = Math.floor(absolute / 60);
  const mins = absolute % 60;

  return `${includeSign ? sign : ""}${hours.toString().padStart(2, "0")}:${mins
    .toString()
    .padStart(2, "0")}`;
}

export type TimeDisplayMode = "standard" | "hours" | "minutes";

export function formatHabitTime(
  minutes: number,
  mode: TimeDisplayMode
): string {
  const days = Math.floor(minutes / 1440);
  const remainingMinutesAfterDays = minutes % 1440;

  const hours = Math.floor(remainingMinutesAfterDays / 60);
  const mins = remainingMinutesAfterDays % 60;

  switch (mode) {
    case "standard": {
      const parts: string[] = [];

      if (days > 0) {
        parts.push(`${days}d`);
      }

      if (hours > 0) {
        parts.push(`${hours}h`);
      }

      if (mins > 0) {
        parts.push(`${mins}m`);
      }

      return parts.length > 0 ? parts.join(" ") : "0 minutes";
    }

    case "hours":
      return `${(minutes / 60).toFixed(1)} hours`;

    case "minutes":
      return `${minutes} minutes`;
  }
}

export function formatRelativeDate(date: string): string {
  const now = new Date();

  const created = new Date(date);

  const diffMs = now.getTime() - created.getTime();

  const seconds = Math.floor(diffMs / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  const weeks = Math.floor(days / 7);
  const months = Math.floor(days / 30);
  const years = Math.floor(days / 365);

  if (seconds < 60) return "just now";

  if (minutes < 60) return `${minutes}m ago`;

  if (hours < 24) return `${hours}h ago`;

  if (days < 7) return `${days}d ago`;

  if (weeks < 5) return `${weeks}w ago`;

  if (months < 12) return `${months}mo ago`;

  return `${years}y ago`;
}
