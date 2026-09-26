import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import logoSvg from "../assets/spanvia-logo.svg";

function SignUp({ onSignUpSuccess }) {
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleSignUp = (e) => {
    e.preventDefault();
    setErrorMessage("");

    if (!fullName.trim() || !email.trim() || !password || !confirmPassword) {
      setErrorMessage("Please fill in all required fields.");
      return;
    }

    if (!email.includes("@") || !email.includes(".")) {
      setErrorMessage("Please enter a valid email address.");
      return;
    }

    if (password.length < 6) {
      setErrorMessage("Password must be at least 6 characters long.");
      return;
    }

    if (password !== confirmPassword) {
      setErrorMessage("Password and Confirm Password do not match.");
      return;
    }

    setLoading(true);

    setTimeout(() => {
      const newUser = {
        name: fullName.trim(),
        email: email.trim(),
        role: "Heritage Explorer",
      };

      if (onSignUpSuccess) {
        onSignUpSuccess(newUser);
      }

      setLoading(false);
      navigate("/");
    }, 600);
  };

  return (
    <div className="login-page">
      <div className="login-card-container">
        <div className="login-header">
          <img src={logoSvg} alt="SPANVIA Heritage Tourism" className="auth-logo-img" />
          <p className="subtitle">Create your SPANVIA Explorer Account</p>
        </div>

        {errorMessage && <div className="error-alert">{errorMessage}</div>}

        <form onSubmit={handleSignUp} className="login-form">
          <div className="form-group">
            <label htmlFor="fullName">Full Name</label>
            <input
              id="fullName"
              type="text"
              placeholder="Enter your full name"
              value={fullName}
              onChange={(e) => setFullName(e.target.value)}
              required
            />
          </div>

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
                placeholder="Create a password (min 6 chars)"
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

          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm Password</label>
            <div className="password-input-wrapper">
              <input
                id="confirmPassword"
                type={showConfirmPassword ? "text" : "password"}
                placeholder="Re-enter your password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
              <button
                type="button"
                className="btn-toggle-password"
                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                title={showConfirmPassword ? "Hide password" : "Show password"}
              >
                {showConfirmPassword ? "👁️‍🗨️" : "👁️"}
              </button>
            </div>
          </div>

          <button type="submit" className="btn-primary-login" disabled={loading}>
            {loading ? "Creating Account..." : "Sign Up for SPANVIA"}
          </button>
        </form>

        <div className="login-footer">
          <p>
            Already have an account?{" "}
            <Link to="/login" className="auth-link">
              Login
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default SignUp;
