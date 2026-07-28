import { Link, NavLink } from "react-router-dom";
import { UserCircle2 } from "lucide-react";
import oakTree from "../../assets/svg/oaktree.svg";

import "../../styles/layout/Navbar.css";

export default function Navbar() {
  return (
    <header className="navbar">
      <div className="navbar-logo">
        <Link to="/">
          <img src={oakTree} alt="Oak tree" className="navbar-logo-icon" />
          <span>Eiki</span>
        </Link>
      </div>

      <nav className="navbar-links">
        <NavLink to="/">Home</NavLink>

        <NavLink to="/habits">Habits</NavLink>

        <button className="navbar-disabled-link">
          Calendar
          <span className="soon-badge">Soon</span>
        </button>

        <button className="navbar-disabled-link">
          Statistics
          <span className="soon-badge">Soon</span>
        </button>
      </nav>

      <div className="navbar-user">
        <NavLink to="/profile">
          <UserCircle2 size={30} />
        </NavLink>
      </div>
    </header>
  );
}
