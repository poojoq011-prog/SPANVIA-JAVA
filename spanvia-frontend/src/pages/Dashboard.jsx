import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api";
import SiteCard from "../components/SiteCard";
import Loading from "../components/Loading";

function Dashboard() {
  const [sites, setSites] = useState([]);
  const [stats, setStats] = useState({ totalSites: 0, unescoSites: 0, statesCovered: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchQuery, setSearchQuery] = useState("");
  const navigate = useNavigate();

  const loadData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [allSites, siteStats] = await Promise.all([
        api.getAllSites(),
        api.getStats().catch(() => null),
      ]);

      setSites(allSites || []);

      if (siteStats) {
        setStats(siteStats);
      } else if (allSites) {
        const unesco = allSites.filter((s) => s.unesco).length;
        const states = new Set(allSites.map((s) => s.state)).size;
        setStats({ totalSites: allSites.length, unescoSites: unesco, statesCovered: states });
      }
    } catch (err) {
      setError(err.message || "Failed to connect to backend server.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleHeroSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/explore?q=${encodeURIComponent(searchQuery.trim())}`);
    }
  };

  const featuredSites = sites.slice(0, 6);

  return (
    <div className="dashboard-page">
      <section className="hero-banner">
        <div className="hero-content">
          <span className="hero-badge">🏛️ DISCOVER INDIA'S RICH CULTURAL HERITAGE</span>
          <h1>Explore Ancient Wonders, Sacred Temples & Timeless Forts</h1>
          <p>
            SPANVIA connects travel enthusiasts to over 100 historical places, vibrant cultural festivals, and tailored travel itineraries across India.
          </p>

          <form onSubmit={handleHeroSearch} className="hero-search-form">
            <input
              type="text"
              placeholder="Search by monument, state (e.g. Rajasthan, Hampi), or festival..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <button type="submit" className="btn-hero-search">
              Search Heritage
            </button>
          </form>
        </div>
      </section>

      <section className="stats-container">
        <div className="stat-card">
          <div className="stat-icon">🚩</div>
          <div className="stat-info">
            <span className="stat-number">{stats.totalSites}</span>
            <span className="stat-label">Heritage Sites Loaded</span>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">✨</div>
          <div className="stat-info">
            <span className="stat-number">{stats.unescoSites}</span>
            <span className="stat-label">UNESCO World Heritage Sites</span>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">📍</div>
          <div className="stat-info">
            <span className="stat-number">{stats.statesCovered}</span>
            <span className="stat-label">Indian States Covered</span>
          </div>
        </div>
      </section>

      <section className="features-grid-section">
        <div className="section-header">
          <h2>Platform Highlights</h2>
          <p>Explore tools designed for heritage preservation and smart tourism</p>
        </div>

        <div className="features-grid">
          <div className="feature-box">
            <div className="feature-icon">🔍</div>
            <h3>Explore 100+ Monuments</h3>
            <p>Access full detailed histories, travel times, and accessibility metrics loaded from our Core Java database.</p>
            <Link to="/explore" className="btn-feature-link">Browse Catalog →</Link>
          </div>

          <div className="feature-box">
            <div className="feature-icon">🤖</div>
            <h3>AI Heritage Recommendation</h3>
            <p>Smart multi-criteria matching based on preferred state, budget, accessibility, rating, and travel season.</p>
            <Link to="/recommendations" className="btn-feature-link">Get AI Plan →</Link>
          </div>

          <div className="feature-box">
            <div className="feature-icon">💰</div>
            <h3>Budget Traveler Planner</h3>
            <p>Filter heritage destinations by maximum budget (e.g. ₹1,500 - ₹5,000) for affordable trips.</p>
            <Link to="/recommendations" className="btn-feature-link">Plan Budget →</Link>
          </div>

          <div className="feature-box">
            <div className="feature-icon">🎊</div>
            <h3>Cultural Festival Explorer</h3>
            <p>Discover vibrant Indian heritage celebrations, Bihu, Hampi Utsav, Shivaratri, and local fairs.</p>
            <Link to="/festivals" className="btn-feature-link">View Festivals →</Link>
          </div>
        </div>
      </section>

      <section className="featured-sites-section">
        <div className="section-header flex-header">
          <div>
            <h2>Featured Heritage Destinations</h2>
            <p>Real-time data served directly from SpanviaService Java Backend</p>
          </div>
          <Link to="/explore" className="btn-view-all">View All 104 Sites →</Link>
        </div>

        {loading ? (
          <Loading message="Fetching heritage sites from Java backend..." />
        ) : error ? (
          <Loading error={error} onRetry={loadData} />
        ) : (
          <div className="site-cards-grid">
            {featuredSites.map((site) => (
              <SiteCard key={site.id} site={site} />
            ))}
          </div>
        )}
      </section>
    </div>
  );
}

export default Dashboard;