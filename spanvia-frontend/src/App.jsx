import { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import Login from "./pages/Login";
import SignUp from "./pages/SignUp";
import Dashboard from "./pages/Dashboard";
import Explore from "./pages/Explore";
import SiteDetails from "./pages/SiteDetails";
import Recommendations from "./pages/Recommendations";
import Festivals from "./pages/Festivals";
import Profile from "./pages/Profile";
import Chatbot from "./components/Chatbot";
import logoSvg from "./assets/spanvia-logo.svg";
import "./index.css";

function App() {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem("spanvia_user");
    return saved ? JSON.parse(saved) : null;
  });

  const handleAuthSuccess = (userData) => {
    setUser(userData);
    localStorage.setItem("spanvia_user", JSON.stringify(userData));
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem("spanvia_user");
  };

  return (
    <Router>
      <div className="app-layout">
        {user && <Navbar user={user} onLogout={handleLogout} />}

        <main className="main-content-container">
          <Routes>
            <Route
              path="/login"
              element={!user ? <Login onLoginSuccess={handleAuthSuccess} /> : <Navigate to="/" replace />}
            />
            <Route
              path="/signup"
              element={!user ? <SignUp onSignUpSuccess={handleAuthSuccess} /> : <Navigate to="/" replace />}
            />
            <Route
              path="/"
              element={user ? <Dashboard /> : <Navigate to="/login" replace />}
            />
            <Route
              path="/explore"
              element={user ? <Explore /> : <Navigate to="/login" replace />}
            />
            <Route
              path="/sites/:id"
              element={user ? <SiteDetails /> : <Navigate to="/login" replace />}
            />
            <Route
              path="/recommendations"
              element={user ? <Recommendations /> : <Navigate to="/login" replace />}
            />
            <Route
              path="/festivals"
              element={user ? <Festivals /> : <Navigate to="/login" replace />}
            />
            <Route
              path="/profile"
              element={user ? <Profile user={user} onUserUpdate={handleAuthSuccess} onLogout={handleLogout} /> : <Navigate to="/login" replace />}
            />
            <Route path="*" element={<Navigate to={user ? "/" : "/login"} replace />} />
          </Routes>
        </main>

        {user && <Chatbot />}

        <footer className="app-footer">
          <div className="footer-container">
            <div className="footer-brand">
              <img src={logoSvg} alt="SPANVIA" className="footer-logo-img" />
            </div>
            <p className="footer-copy">
              Powered by React + Vite & Core Java Backend • Heritage Places Dataset
            </p>
          </div>
        </footer>
      </div>
    </Router>
  );
}

export default App;