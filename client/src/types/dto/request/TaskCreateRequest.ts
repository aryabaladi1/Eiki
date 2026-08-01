import { TaskDifficulty } from "../../enums/TaskDifficulty";

export interface TaskCreateRequest {
  name: string;
  description?: string | null;
  difficulty?: TaskDifficulty;
  dueDate?: string | null;
}
