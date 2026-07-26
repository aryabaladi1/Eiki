import "../../styles/common/ConfirmationModal.css";

interface ConfirmationModalProps {
  open: boolean;

  title: string;
  message: string;

  confirmText?: string;
  cancelText?: string;

  danger?: boolean;
  loading?: boolean;

  onConfirm: () => void;
  onClose: () => void;
}

export default function ConfirmationModal({
  open,
  title,
  message,
  confirmText = "Confirm",
  cancelText = "Cancel",
  danger = false,
  loading = false,
  onConfirm,
  onClose,
}: ConfirmationModalProps) {
  if (!open) {
    return null;
  }

  return (
    <div className="confirmation-overlay" onClick={onClose}>
      <div className="confirmation-modal" onClick={(e) => e.stopPropagation()}>
        <h2>{title}</h2>

        <p>{message}</p>

        <div className="confirmation-actions">
          <button
            className="confirmation-cancel"
            onClick={onClose}
            disabled={loading}
          >
            {cancelText}
          </button>

          <button
            className={`confirmation-confirm ${
              danger ? "confirmation-danger" : ""
            }`}
            onClick={onConfirm}
            disabled={loading}
          >
            {loading ? "Loading..." : confirmText}
          </button>
        </div>
      </div>
    </div>
  );
}
