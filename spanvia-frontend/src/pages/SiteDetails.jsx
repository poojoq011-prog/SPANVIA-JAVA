import { useState, useEffect } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import api from "../services/api";
import Loading from "../components/Loading";

function SiteDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [site, setSite] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchSite = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.getSiteById(id);
      setSite(data);
    } catch (err) {
      setError(err.message || `Failed to fetch details for site #${id}`);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSite();
  }, [id]);

  if (loading) return <Loading message="Loading site details from backend..." />;
  if (error) return <Loading error={error} onRetry={fetchSite} />;
  if (!site) return <div className="empty-state">Site not found.</div>;

  return (
    <div className="site-details-page">
      <div className="back-link-container">
        <button onClick={() => navigate(-1)} className="btn-back">
          ← Back to Search
        </button>
        <span className="breadcrumb-path">
          <Link to="/">Dashboard</Link> / <Link to="/explore">Sites</Link> / {site.name}
        </span>
      </div>

      <div className="details-header-banner">
        <div className="details-banner-content">
          <div className="header-badges">
            <span className="badge category-badge">📍 {site.category}</span>
            {site.unesco && <span className="badge unesco-badge">✨ UNESCO World Heritage Site</span>}
          </div>
          <h1 className="details-title">{site.name}</h1>
          <p className="details-subtitle">
            <span className="icon">📍</span> {site.state}, India
          </p>
        </div>
      </div>

      <div className="quick-stats-bar details-stats-margin">
        <div className="quick-stat">
          <span className="stat-label">Visitor Rating</span>
          <span className="stat-val rating-val">⭐ {site.rating} / 5.0</span>
        </div>

        <div className="quick-stat">
          <span className="stat-label">Estimated Budget</span>
          <span className="stat-val budget-val">₹{site.budget ? site.budget.toLocaleString() : site.estimatedBudget || 0}</span>
        </div>

        <div className="quick-stat">
          <span className="stat-label">Best Travel Season</span>
          <span className="stat-val">🗓️ {site.bestTime}</span>
        </div>

        <div className="quick-stat">
          <span className="stat-label">Accessibility</span>
          <span className="stat-val">🚶 {site.accessibility}</span>
        </div>
      </div>

      <div className="details-grid-container">
        <div className="details-main-content">
          <section className="details-section">
            <h2>📜 Historical Background</h2>
            <p className="history-text">{site.history}</p>
          </section>

          <section className="details-section">
            <h2>🎊 Cultural Festivals & Traditions</h2>
            <p>
              This heritage site is prominently associated with the <strong>{site.festival || "Local Cultural Celebrations"}</strong> festival, bringing thousands of visitors and traditional music, dance, and rituals.
            </p>
          </section>

          <section className="details-section">
            <h2>📍 Nearby Cultural Attractions</h2>
            <p>
              When visiting {site.name}, be sure to explore <strong>{site.nearbyAttraction || "surrounding historical landscapes and temples"}</strong>.
            </p>
          </section>

          <section className="details-section tips-box">
            <h2>🧳 Traveler Guidance & Travel Tips</h2>
            <ul>
              <li><strong>Best Months:</strong> {site.bestTime}</li>
              <li><strong>Accessibility Status:</strong> {site.accessibility}</li>
              <li><strong>Recommended Activity:</strong> Photography, guided heritage walks, and historical exploration.</li>
              <li><strong>General Advice:</strong> Carry adequate drinking water, wear comfortable walking footwear, and preserve local cleanliness.</li>
            </ul>
          </section>
        </div>

        <aside className="details-sidebar">
          <div className="sidebar-card rules-card">
            <h3>🏛️ SPANVIA Heritage Preservation Rules</h3>
            <p className="rules-subtitle">Preserving our cultural legacy for future generations:</p>
            <ol>
              <li>Do not damage, deface, or carve on ancient structures or monuments.</li>
              <li>Avoid littering; use designated recycling bins.</li>
              <li>Respect local cultural traditions and religious customs.</li>
              <li>Strictly follow official photography and drone guidelines.</li>
            </ol>
          </div>

          <div className="sidebar-card action-card">
            <h3>Plan Your Trip</h3>
            <p>Find affordable travel routes and options within ₹{site.budget ? site.budget.toLocaleString() : site.estimatedBudget || 0}</p>
            <Link to="/recommendations" className="btn-plan-trip">
              Budget Planner →
            </Link>
          </div>
        </aside>
      </div>
    </div>
  );
}

export default SiteDetails;
