import { useEffect, useRef, useState } from "react";

import "../../styles/weeks/EditableNumberCell.css";
import { minutesToTime } from "../../utils/time";

interface EditableNumberCellProps {
  value?: number | null;
  mode?: "number" | "time";
  disabled?: boolean;
  onSave: (value: number) => void;
}

export default function EditableNumberCell({
  value,
  mode = "number",
  disabled,
  onSave,
}: EditableNumberCellProps) {
  const [editing, setEditing] = useState(false);

  const [draft, setDraft] = useState("");

  const inputRef = useRef<HTMLInputElement>(null);

  function parseTime(text: string): number | null {
    const value = text.trim();

    // 2:40
    // 02:40

    const colonMatch = /^(\d{1,2}):(\d{2})$/.exec(value);

    if (colonMatch) {
      const hours = Number(colonMatch[1]);
      const minutes = Number(colonMatch[2]);

      if (minutes > 59) {
        return null;
      }

      if (hours > 23) {
        return null;
      }

      return hours * 60 + minutes;
    }

    // 40
    // 240
    // 0230

    const digits = value.replace(/\D/g, "");

    if (digits.length === 1) {
      // 5 -> 00:05
      return Number(digits);
    }

    if (digits.length === 2) {
      // 40 -> 00:40

      const minutes = Number(digits);

      if (minutes > 59) {
        return null;
      }

      return minutes;
    }

    if (digits.length === 3) {
      // 240 -> 02:40

      const hours = Number(digits[0]);
      const minutes = Number(digits.slice(1));

      if (minutes > 59 || hours > 23) {
        return null;
      }

      return hours * 60 + minutes;
    }

    if (digits.length === 4) {
      // 0230 -> 02:30
      // 1230 -> 12:30

      const hours = Number(digits.slice(0, 2));
      const minutes = Number(digits.slice(2));

      if (minutes > 59 || hours > 23) {
        return null;
      }

      return hours * 60 + minutes;
    }

    return null;
  }

  useEffect(() => {
    if (mode === "time") {
      setDraft(minutesToTime(value ?? 0));
    } else {
      setDraft(value?.toString() ?? "");
    }
  }, [value, mode]);

  useEffect(() => {
    if (editing) {
      inputRef.current?.focus();
      inputRef.current?.select();
    }
  }, [editing]);

  function save() {
    if (mode === "time") {
      const parsed = parseTime(draft);

      if (parsed === null) {
        setDraft(minutesToTime(value ?? 0));

        setEditing(false);

        return;
      }

      onSave(parsed);
    } else {
      const number = Number(draft);

      if (!Number.isNaN(number)) {
        onSave(number);
      }
    }

    setEditing(false);
  }

  if (editing) {
    return (
      <input
        ref={inputRef}
        className="editable-number-input"
        type="text"
        value={draft}
        placeholder={mode === "time" ? "00:00" : ""}
        onChange={(e) => {
          if (mode === "time") {
            setDraft(e.target.value.replace(/[^\d:]/g, ""));
          } else {
            setDraft(e.target.value);
          }
        }}
        onBlur={save}
        onKeyDown={(e) => {
          if (e.key === "Enter") {
            save();
          }

          if (e.key === "Escape") {
            setDraft(
              mode === "time"
                ? minutesToTime(value ?? 0)
                : value?.toString() ?? ""
            );

            setEditing(false);
          }
        }}
      />
    );
  }

  return (
    <div
      className={`editable-number-display ${disabled ? "disabled" : ""}`}
      onClick={() => {
        if (!disabled) {
          setEditing(true);
        }
      }}
    >
      {mode === "time" ? minutesToTime(value ?? 0) : value ?? 0}
    </div>
  );
}
