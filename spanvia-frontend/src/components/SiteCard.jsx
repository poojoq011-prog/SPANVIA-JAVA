import { Link } from "react-router-dom";

function SiteCard({ site }) {
  if (!site) return null;

  const getCategoryIcon = (cat) => {
    if (!cat) return "📍";
    const lower = cat.toLowerCase();
    if (lower.includes("temple")) return "🛕";
    if (lower.includes("fort")) return "🏰";
    if (lower.includes("palace")) return "👑";
    if (lower.includes("cave") || lower.includes("rock")) return "🗿";
    if (lower.includes("archaeological") || lower.includes("ruins")) return "🏛️";
    if (lower.includes("monastery") || lower.includes("buddhist")) return "☸️";
    if (lower.includes("church")) return "⛪";
    if (lower.includes("mosque")) return "🕌";
    return "🚩";
  };

  return (
    <div className="site-card">
      <div className="site-card-body">
        <div className="site-card-header-badges">
          <span className="site-card-badge category-badge">
            {getCategoryIcon(site.category)} {site.category}
          </span>
          {site.unesco && (
            <span className="site-card-badge unesco-badge" title="UNESCO World Heritage Site">
              ✨ UNESCO
            </span>
          )}
        </div>

        <h3 className="site-title">{site.name}</h3>
        <p className="site-location">
          <span className="location-icon">📍</span> {site.state}
        </p>

        <div className="site-meta-grid">
          <div className="meta-item">
            <span className="meta-label">Est. Budget</span>
            <span className="meta-value budget-value">₹{site.budget ? site.budget.toLocaleString() : site.estimatedBudget || 0}</span>
          </div>
          <div className="meta-item">
            <span className="meta-label">Rating</span>
            <span className="meta-value rating-value">⭐ {site.rating || 4.5}</span>
          </div>
        </div>

        <div className="site-quick-info">
          {site.bestTime && (
            <span className="info-chip">🗓️ {site.bestTime}</span>
          )}
          {site.accessibility && (
            <span className="info-chip">🚶 {site.accessibility}</span>
          )}
        </div>
      </div>

      <div className="site-card-footer">
        <Link to={`/sites/${site.id}`} className="btn-view-details">
          View Details <span>→</span>
        </Link>
      </div>
    </div>
  );
}

export default SiteCard;

