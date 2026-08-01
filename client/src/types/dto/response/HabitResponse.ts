export interface HabitResponse {
  id: number;
  userId: number;
  name: string;
  description?: string | null;
  minutesTotal: number;
  archived: boolean;
  archivedAt: string;
  createdAt: string;
}
