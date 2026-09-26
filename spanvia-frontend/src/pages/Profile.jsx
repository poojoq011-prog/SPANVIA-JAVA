import { useState, useEffect } from "react";
import { Link } from "react-router-dom";

const AVATAR_OPTIONS = ["🏛️", "🕌", "🏰", "🗿", "🌄", "🛕", "⛩️", "⚔️", "🌴"];

function Profile({ user, onUserUpdate, onLogout }) {
  // Load profile with localStorage persistence or fallback to user props
  const [profile, setProfile] = useState(() => {
    const saved = localStorage.getItem("spanvia_profile_data");
    if (saved) {
      try {
        const parsed = JSON.parse(saved);
        return {
          ...parsed,
          name: user?.name || parsed.name || "Pooja Sharma",
          email: user?.email || parsed.email || "pooja.sharma@spanvia.com",
        };
      } catch (e) {
        console.error("Error parsing saved profile data:", e);
      }
    }
    return {
      name: user?.name || "Pooja Sharma",
      email: user?.email || "pooja.sharma@spanvia.com",
      phone: "+91 98765 43210",
      location: "Bengaluru, Karnataka",
      avatar: "🏛️",
      photoUrl: "",
      travelStyle: "Cultural & Historical Explorer",
      preferredState: "Karnataka",
      preferredCategory: "Temple & Fort",
      maxBudgetTier: "₹3,500 - ₹5,000",
      favSeason: "October to March",
      memberSince: "September 2025",
    };
  });

  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState({ ...profile });

  // Sync profile when user prop updates
  useEffect(() => {
    if (user?.name && user.name !== profile.name) {
      setProfile((prev) => ({ ...prev, name: user.name, email: user.email || prev.email }));
    }
  }, [user]);

  // Interactive Saved Places with local storage
  const [savedSites, setSavedSites] = useState(() => {
    const saved = localStorage.getItem("spanvia_saved_sites");
    if (saved) {
      try { return JSON.parse(saved); } catch (e) { console.error(e); }
    }
    return [
      { id: 1, name: "Brihadeeswarar Temple", state: "Tamil Nadu", category: "Temple", rating: 4.9, budget: 3000, unesco: true },
      { id: 2, name: "Hampi", state: "Karnataka", category: "Fort", rating: 4.8, budget: 5000, unesco: true },
      { id: 34, name: "Bhimbetka Rock Shelters", state: "Madhya Pradesh", category: "Rock Shelters", rating: 4.8, budget: 2500, unesco: true },
      { id: 105, name: "Jaisalmer Fort", state: "Rajasthan", category: "Fort", rating: 4.8, budget: 3500, unesco: true },
    ];
  });

  // Interactive Visited Places with local storage
  const [visitedSites, setVisitedSites] = useState(() => {
    const saved = localStorage.getItem("spanvia_visited_sites");
    if (saved) {
      try { return JSON.parse(saved); } catch (e) { console.error(e); }
    }
    return [
      { id: 17, name: "Adalaj Stepwell", state: "Gujarat", visitedDate: "Nov 2025" },
      { id: 28, name: "Badami Cave Temples", state: "Karnataka", visitedDate: "Jan 2026" },
      { id: 91, name: "Elephanta Caves", state: "Maharashtra", visitedDate: "Feb 2026" },
    ];
  });

  const [newVisitedName, setNewVisitedName] = useState("");
  const [newVisitedState, setNewVisitedState] = useState("");

  const handleStartEdit = () => {
    setEditForm({ ...profile });
    setIsEditing(true);
  };

  const handleSaveEdit = (e) => {
    e.preventDefault();
    const updated = { ...editForm };
    setProfile(updated);
    localStorage.setItem("spanvia_profile_data", JSON.stringify(updated));

    if (onUserUpdate) {
      onUserUpdate({ name: updated.name, email: updated.email });
    }
    setIsEditing(false);
  };

  const handleRemoveSaved = (id) => {
    const updated = savedSites.filter((s) => s.id !== id);
    setSavedSites(updated);
    localStorage.setItem("spanvia_saved_sites", JSON.stringify(updated));
  };

  const handleAddVisited = (e) => {
    e.preventDefault();
    if (!newVisitedName.trim()) return;
    const now = new Date();
    const monthYear = now.toLocaleString("default", { month: "short", year: "numeric" });

    const newEntry = {
      id: Date.now(),
      name: newVisitedName.trim(),
      state: newVisitedState.trim() || "India",
      visitedDate: monthYear,
    };
    const updated = [newEntry, ...visitedSites];
    setVisitedSites(updated);
    localStorage.setItem("spanvia_visited_sites", JSON.stringify(updated));
    setNewVisitedName("");
    setNewVisitedState("");
  };

  const handleRemoveVisited = (id) => {
    const updated = visitedSites.filter((v) => v.id !== id);
    setVisitedSites(updated);
    localStorage.setItem("spanvia_visited_sites", JSON.stringify(updated));
  };

  return (
    <div className="profile-page">
      {/* Banner / Page Header */}
      <div className="profile-hero-banner">
        <div className="profile-banner-content">
          <div className="profile-banner-badge">
            <span>✨ SPANVIA Explorer Account</span>
          </div>
          <h1>User Profile & Heritage Dashboard</h1>
          <p>Manage your personal details, travel preferences, and saved heritage destinations</p>
        </div>

        <div className="profile-top-actions">
          <button
            className={`btn-edit-profile ${isEditing ? "active" : ""}`}
            onClick={isEditing ? () => setIsEditing(false) : handleStartEdit}
          >
            {isEditing ? "✖ Cancel Editing" : "✏️ Edit Profile"}
          </button>
          <button className="btn-logout-profile" onClick={onLogout} title="Log out of SPANVIA">
            Logout 🚪
          </button>
        </div>
      </div>

      {/* Edit Profile Form Card */}
      {isEditing && (
        <section className="edit-profile-modal-card">
          <div className="edit-card-header">
            <h2>✏️ Edit Personal Details & Preferences</h2>
            <p>Update your display name, contact info, avatar and travel preferences</p>
          </div>

          <form onSubmit={handleSaveEdit} className="edit-profile-form">
            {/* Avatar Picker */}
            <div className="avatar-picker-container">
              <label className="picker-label">Choose Profile Avatar / Icon</label>
              <div className="avatar-chips-grid">
                {AVATAR_OPTIONS.map((emoji) => (
                  <button
                    key={emoji}
                    type="button"
                    className={`avatar-chip ${editForm.avatar === emoji && !editForm.photoUrl ? "selected" : ""}`}
                    onClick={() => setEditForm({ ...editForm, avatar: emoji, photoUrl: "" })}
                  >
                    {emoji}
                  </button>
                ))}
              </div>

              <div className="custom-photo-input">
                <label>Or Image Photo URL (Optional)</label>
                <input
                  type="url"
                  placeholder="https://example.com/photo.jpg"
                  value={editForm.photoUrl || ""}
                  onChange={(e) => setEditForm({ ...editForm, photoUrl: e.target.value })}
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Full Name</label>
                <input
                  type="text"
                  value={editForm.name}
                  onChange={(e) => setEditForm({ ...editForm, name: e.target.value })}
                  required
                />
              </div>

              <div className="form-group">
                <label>Email Address</label>
                <input
                  type="email"
                  value={editForm.email}
                  onChange={(e) => setEditForm({ ...editForm, email: e.target.value })}
                  required
                />
              </div>

              <div className="form-group">
                <label>Phone Number</label>
                <input
                  type="text"
                  value={editForm.phone}
                  onChange={(e) => setEditForm({ ...editForm, phone: e.target.value })}
                />
              </div>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label>Location / City</label>
                <input
                  type="text"
                  value={editForm.location}
                  onChange={(e) => setEditForm({ ...editForm, location: e.target.value })}
                />
              </div>

              <div className="form-group">
                <label>Travel Style</label>
                <select
                  value={editForm.travelStyle}
                  onChange={(e) => setEditForm({ ...editForm, travelStyle: e.target.value })}
                >
                  <option value="Cultural & Historical Explorer">Cultural & Historical Explorer</option>
                  <option value="Backpacker & Budget Traveler">Backpacker & Budget Traveler</option>
                  <option value="Architecture Enthusiast">Architecture Enthusiast</option>
                  <option value="Spiritual & Pilgrimage Traveler">Spiritual & Pilgrimage Traveler</option>
                  <option value="UNESCO Heritage Collector">UNESCO Heritage Collector</option>
                </select>
              </div>

              <div className="form-group">
                <label>Preferred Heritage State</label>
                <input
                  type="text"
                  value={editForm.preferredState}
                  onChange={(e) => setEditForm({ ...editForm, preferredState: e.target.value })}
                />
              </div>
            </div>

            <div className="form-actions">
              <button type="button" className="btn-cancel-profile" onClick={() => setIsEditing(false)}>
                Cancel
              </button>
              <button type="submit" className="btn-save-profile">
                💾 Save Profile Changes
              </button>
            </div>
          </form>
        </section>
      )}

      {/* Main Profile Grid */}
      <div className="profile-grid">
        {/* Sidebar Column */}
        <aside className="profile-sidebar-column">
          {/* Main User Card */}
          <div className="profile-card profile-user-card text-center">
            <div className="avatar-wrapper">
              {profile.photoUrl ? (
                <img
                  src={profile.photoUrl}
                  alt={profile.name}
                  className="avatar-image-large"
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.style.display = "none";
                  }}
                />
              ) : (
                <div className="avatar-large">{profile.avatar || "🏛️"}</div>
              )}
            </div>
            <h2>{profile.name}</h2>
            <p className="user-email">{profile.email}</p>
            <span className="badge role-badge">SPANVIA Explorer</span>

            <div className="profile-stats">
              <div className="p-stat">
                <span className="stat-num">{savedSites.length}</span>
                <span className="stat-lbl">Saved</span>
              </div>
              <div className="p-stat">
                <span className="stat-num">{visitedSites.length}</span>
                <span className="stat-lbl">Visited</span>
              </div>
              <div className="p-stat">
                <span className="stat-num">Level 5</span>
                <span className="stat-lbl">Badge</span>
              </div>
            </div>
          </div>

          {/* Account Information Card */}
          <div className="profile-card">
            <h3>📱 Personal Information</h3>
            <ul className="info-list">
              <li>
                <span className="info-label">📱 Phone:</span>
                <span className="info-val">{profile.phone}</span>
              </li>
              <li>
                <span className="info-label">📍 Location:</span>
                <span className="info-val">{profile.location}</span>
              </li>
              <li>
                <span className="info-label">📅 Member Since:</span>
                <span className="info-val">{profile.memberSince || "Sept 2025"}</span>
              </li>
              <li>
                <span className="info-label">⚡ Status:</span>
                <span className="info-val status-active">Active Explorer ✓</span>
              </li>
            </ul>
          </div>

          {/* Travel Preferences Card */}
          <div className="profile-card">
            <h3>🧭 Travel Preferences</h3>
            <ul className="info-list">
              <li>
                <span className="info-label">🧭 Style:</span>
                <span className="info-val">{profile.travelStyle}</span>
              </li>
              <li>
                <span className="info-label">📌 Fav State:</span>
                <span className="info-val">{profile.preferredState}</span>
              </li>
              <li>
                <span className="info-label">🏛️ Category:</span>
                <span className="info-val">{profile.preferredCategory}</span>
              </li>
              <li>
                <span className="info-label">💰 Target Budget:</span>
                <span className="info-val">{profile.maxBudgetTier}</span>
              </li>
            </ul>
          </div>
        </aside>

        {/* Details & Interactive Column */}
        <main className="profile-details-column">
          {/* Saved Places Section */}
          <section className="profile-section">
            <div className="flex-header section-title-bar">
              <h2>❤️ Saved / Favourite Heritage Places ({savedSites.length})</h2>
              <Link to="/explore" className="btn-add-more">
                + Discover More
              </Link>
            </div>

            {savedSites.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">🏛️</div>
                <p>No saved heritage destinations yet.</p>
                <Link to="/explore" className="btn-explore-link">
                  Browse Heritage Sites
                </Link>
              </div>
            ) : (
              <div className="saved-sites-grid">
                {savedSites.map((site) => (
                  <div key={site.id} className="saved-site-card">
                    <div className="saved-site-info">
                      <div className="saved-site-header">
                        <h4>{site.name}</h4>
                        {site.unesco && <span className="unesco-tag">UNESCO</span>}
                      </div>
                      <p className="saved-sub">
                        📍 {site.state} • {site.category}
                      </p>
                      <div className="saved-tags">
                        <span className="tag-rating">⭐ {site.rating}</span>
                        <span className="tag-budget">₹{site.budget}</span>
                      </div>
                    </div>
                    <div className="saved-actions">
                      <Link to={`/sites/${site.id}`} className="btn-view-item" title="View Details">
                        View Details →
                      </Link>
                      <button
                        className="btn-remove-item"
                        onClick={() => handleRemoveSaved(site.id)}
                        title="Remove from saved"
                      >
                        ✖
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </section>

          {/* Visited Places Tracker Section */}
          <section className="profile-section">
            <div className="flex-header section-title-bar">
              <h2>🚩 Visited Heritage Places Tracker ({visitedSites.length})</h2>
              <span className="tracker-sub-tag">Keep track of your journeys</span>
            </div>

            <form onSubmit={handleAddVisited} className="add-visited-form">
              <input
                type="text"
                placeholder="Place name (e.g. Red Fort)"
                value={newVisitedName}
                onChange={(e) => setNewVisitedName(e.target.value)}
                required
              />
              <input
                type="text"
                placeholder="State (e.g. Delhi)"
                value={newVisitedState}
                onChange={(e) => setNewVisitedState(e.target.value)}
              />
              <button type="submit" className="btn-add-visited">
                + Mark Visited
              </button>
            </form>

            <div className="visited-sites-list">
              {visitedSites.length === 0 ? (
                <p className="no-visited-msg">No visited places tracked yet. Add your first destination above!</p>
              ) : (
                visitedSites.map((item) => (
                  <div key={item.id} className="visited-item-row">
                    <div className="visited-left">
                      <span className="visited-check">✓</span>
                      <div>
                        <strong className="visited-name">{item.name}</strong>
                        <span className="visited-state">📍 {item.state}</span>
                      </div>
                    </div>
                    <div className="visited-right">
                      <span className="visited-date">{item.visitedDate}</span>
                      <button
                        className="btn-remove-item"
                        onClick={() => handleRemoveVisited(item.id)}
                        title="Remove from visited"
                      >
                        ✖
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>
          </section>
        </main>
      </div>
    </div>
  );
}

export default Profile;
