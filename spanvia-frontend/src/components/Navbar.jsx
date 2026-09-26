import { Link, useNavigate, useLocation } from "react-router-dom";
import logoSvg from "../assets/spanvia-logo.svg";

function Navbar({ user, onLogout }) {
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    if (onLogout) onLogout();
    navigate("/login");
  };

  const isActive = (path) => location.pathname === path;

  return (
    <header className="navbar-header">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          <img src={logoSvg} alt="SPANVIA Heritage Tourism" className="navbar-logo-img" />
        </Link>

        <nav className="navbar-nav">
          <Link to="/" className={`nav-item ${isActive("/") ? "active" : ""}`}>
            Dashboard
          </Link>
          <Link to="/explore" className={`nav-item ${isActive("/explore") ? "active" : ""}`}>
            Explore Sites
          </Link>
          <Link to="/recommendations" className={`nav-item ${isActive("/recommendations") ? "active" : ""}`}>
            AI & Budget
          </Link>
          <Link to="/festivals" className={`nav-item ${isActive("/festivals") ? "active" : ""}`}>
            Festivals
          </Link>
          <Link to="/profile" className={`nav-item ${isActive("/profile") ? "active" : ""}`}>
            Profile
          </Link>
        </nav>

        <div className="navbar-actions">
          {user ? (
            <div className="user-profile-badge">
              <span className="user-avatar">👤</span>
              <span className="user-name">{user.name || "Explorer"}</span>
              <button onClick={handleLogout} className="btn-logout" title="Logout">
                Logout
              </button>
            </div>
          ) : (
            <Link to="/login" className="btn-login">
              Login
            </Link>
          )}
        </div>
      </div>
    </header>
  );
}

export default Navbar;
