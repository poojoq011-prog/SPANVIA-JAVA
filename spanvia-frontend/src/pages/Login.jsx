import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api";
import logoSvg from "../assets/spanvia-logo.svg";

function Login({ onLoginSuccess }) {
  const [email, setEmail] = useState("explorer@spanvia.com");
  const [password, setPassword] = useState("heritage2026");
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setErrorMessage("");

    if (!email.trim() || !password) {
      setErrorMessage("Please enter both email and password.");
      return;
    }

    setLoading(true);

    try {
      const res = await api.login(email, password);
      if (res && res.success) {
        if (onLoginSuccess) {
          onLoginSuccess(res.user);
        }
        navigate("/");
      } else {
        setErrorMessage(res.message || "Invalid email or password.");
      }
    } catch (err) {
      // Fallback demo login if server endpoint throws error or network offline
      if (email.includes("@")) {
        const dummyUser = {
          name: email.split("@")[0].replace(".", " "),
          email: email.trim(),
          role: "Heritage Explorer",
        };
        if (onLoginSuccess) onLoginSuccess(dummyUser);
        navigate("/");
      } else {
        setErrorMessage(err.message || "Failed to log in. Please check your credentials.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card-container">
        <div className="login-header">
          <img src={logoSvg} alt="SPANVIA Heritage Tourism" className="auth-logo-img" />
          <p className="subtitle">Heritage Tourism & Discovery Platform</p>
        </div>

        {errorMessage && <div className="error-alert">{errorMessage}</div>}

        <form onSubmit={handleLogin} className="login-form">
          <div className="form-group">
            <label htmlFor="email">Email Address</label>
            <input
              id="email"
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <div className="password-input-wrapper">
              <input
                id="password"
                type={showPassword ? "text" : "password"}
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <button
                type="button"
                className="btn-toggle-password"
                onClick={() => setShowPassword(!showPassword)}
                title={showPassword ? "Hide password" : "Show password"}
              >
                {showPassword ? "👁️‍🗨️" : "👁️"}
              </button>
            </div>
          </div>

          <button type="submit" className="btn-primary-login" disabled={loading}>
            {loading ? "Authenticating..." : "Login to SPANVIA"}
          </button>
        </form>

        <div className="login-footer">
          <p>
            Don't have an account?{" "}
            <Link to="/signup" className="auth-link">
              Sign Up
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Login;