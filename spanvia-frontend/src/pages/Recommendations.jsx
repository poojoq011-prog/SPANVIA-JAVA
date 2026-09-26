import { useState, useEffect } from "react";
import api from "../services/api";
import SiteCard from "../components/SiteCard";
import Loading from "../components/Loading";

function Recommendations() {
  const [activeTab, setActiveTab] = useState("ai"); // "ai" or "budget"

  // Budget Planner State
  const [budgetInput, setBudgetInput] = useState(3000);
  const [budgetSites, setBudgetSites] = useState([]);
  const [budgetLoading, setBudgetLoading] = useState(false);
  const [budgetError, setBudgetError] = useState(null);

  // AI Recommendation State
  const [aiForm, setAiForm] = useState({
    state: "All",
    category: "All",
    maxBudget: 3500,
    minRating: 4.5,
    accessibility: "All",
    travelSeason: "All",
    festival: "All",
    unesco: false,
  });
  const [aiResults, setAiResults] = useState([]);
  const [aiLoading, setAiLoading] = useState(false);
  const [aiError, setAiError] = useState(null);

  // Available options
  const statesList = ["All", "Tamil Nadu", "Karnataka", "Madhya Pradesh", "Gujarat", "Rajasthan", "Maharashtra", "Odisha", "Assam", "Bihar", "Uttar Pradesh", "Himachal Pradesh"];
  const categoriesList = ["All", "Temple", "Fort", "Palace", "Stepwell", "Archaeological Site", "Rock-cut Temple", "Caves", "Royal Burial Site"];
  const seasonsList = ["All", "October to February", "November to March", "March to June", "October to April"];

  // Handle Budget Planner Search
  const fetchBudgetSites = async (maxB) => {
    setBudgetLoading(true);
    setBudgetError(null);
    try {
      const data = await api.getBudgetRecommendations(maxB);
      setBudgetSites(data || []);
    } catch (err) {
      setBudgetError(err.message || "Failed to fetch budget recommendations.");
    } finally {
      setBudgetLoading(false);
    }
  };

  // Handle AI Search
  const fetchAiRecommendations = async () => {
    setAiLoading(true);
    setAiError(null);
    try {
      const data = await api.getAiRecommendations(aiForm);
      setAiResults(data || []);
    } catch (err) {
      setAiError(err.message || "Failed to fetch AI recommendations.");
    } finally {
      setAiLoading(false);
    }
  };

  useEffect(() => {
    fetchBudgetSites(budgetInput);
    fetchAiRecommendations();
  }, []);

  const handleBudgetSubmit = (e) => {
    e.preventDefault();
    fetchBudgetSites(budgetInput);
  };

  const handleAiSubmit = (e) => {
    e.preventDefault();
    fetchAiRecommendations();
  };

  return (
    <div className="recommendations-page">
      <div className="page-header text-center">
        <h1>Smart Heritage Travel & Budget Recommendations</h1>
        <p>Powered by Java SpanviaService multi-attribute ranking engine</p>

        <div className="tab-switcher">
          <button
            className={`tab-btn ${activeTab === "ai" ? "active" : ""}`}
            onClick={() => setActiveTab("ai")}
          >
            🤖 AI Heritage Recommendation
          </button>
          <button
            className={`tab-btn ${activeTab === "budget" ? "active" : ""}`}
            onClick={() => setActiveTab("budget")}
          >
            💰 Budget Destination Planner
          </button>
        </div>
      </div>

      {activeTab === "ai" && (
        <section className="recommendation-section">
          <div className="form-card-container">
            <h2>Customize Your Heritage Travel Profile</h2>
            <p className="form-subtext">Our recommendation engine matches your preferences across 100+ monuments</p>

            <form onSubmit={handleAiSubmit} className="ai-filter-form">
              <div className="form-row">
                <div className="form-group">
                  <label>Preferred State</label>
                  <select
                    value={aiForm.state}
                    onChange={(e) => setAiForm({ ...aiForm, state: e.target.value })}
                  >
                    {statesList.map((st) => (
                      <option key={st} value={st}>{st}</option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label>Heritage Category</label>
                  <select
                    value={aiForm.category}
                    onChange={(e) => setAiForm({ ...aiForm, category: e.target.value })}
                  >
                    {categoriesList.map((cat) => (
                      <option key={cat} value={cat}>{cat}</option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label>Max Budget: ₹{aiForm.maxBudget}</label>
                  <input
                    type="range"
                    min="1000"
                    max="6000"
                    step="500"
                    value={aiForm.maxBudget}
                    onChange={(e) => setAiForm({ ...aiForm, maxBudget: Number(e.target.value) })}
                  />
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>Min Visitor Rating</label>
                  <select
                    value={aiForm.minRating}
                    onChange={(e) => setAiForm({ ...aiForm, minRating: Number(e.target.value) })}
                  >
                    <option value={0}>Any Rating</option>
                    <option value={4.0}>⭐ 4.0+</option>
                    <option value={4.5}>⭐ 4.5+</option>
                    <option value={4.7}>⭐ 4.7+</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Accessibility</label>
                  <select
                    value={aiForm.accessibility}
                    onChange={(e) => setAiForm({ ...aiForm, accessibility: e.target.value })}
                  >
                    <option value="All">All Access Levels</option>
                    <option value="Easy">Easy</option>
                    <option value="Moderate">Moderate</option>
                    <option value="Challenging">Challenging</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Preferred Travel Season</label>
                  <select
                    value={aiForm.travelSeason}
                    onChange={(e) => setAiForm({ ...aiForm, travelSeason: e.target.value })}
                  >
                    {seasonsList.map((s) => (
                      <option key={s} value={s}>{s}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="form-row flex-row">
                <label className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={aiForm.unesco}
                    onChange={(e) => setAiForm({ ...aiForm, unesco: e.target.checked })}
                  />
                  <span>Require UNESCO World Heritage Status ✨</span>
                </label>

                <button type="submit" className="btn-ai-submit" disabled={aiLoading}>
                  {aiLoading ? "Calculating..." : "Generate AI Recommendations 🚀"}
                </button>
              </div>
            </form>
          </div>

          <div className="results-container">
            <h2 className="results-title">
              Top Matched Recommendations ({aiResults.length})
            </h2>

            {aiLoading ? (
              <Loading message="Evaluating matching heritage algorithms..." />
            ) : aiError ? (
              <Loading error={aiError} onRetry={fetchAiRecommendations} />
            ) : aiResults.length === 0 ? (
              <div className="empty-state">No matching destinations found for strict criteria.</div>
            ) : (
              <div className="site-cards-grid">
                {aiResults.map((site) => (
                  <SiteCard key={site.id} site={site} />
                ))}
              </div>
            )}
          </div>
        </section>
      )}

      {activeTab === "budget" && (
        <section className="budget-planner-section">
          <div className="budget-input-card">
            <h2>Find Places Within Your Maximum Budget</h2>
            <p>Enter your maximum budget threshold to see matching heritage trips</p>

            <form onSubmit={handleBudgetSubmit} className="budget-form">
              <div className="budget-input-group">
                <span className="currency-prefix">₹</span>
                <input
                  type="number"
                  min="500"
                  max="10000"
                  step="250"
                  value={budgetInput}
                  onChange={(e) => setBudgetInput(Number(e.target.value))}
                  placeholder="Enter maximum budget in INR"
                  required
                />
                <button type="submit" className="btn-budget-submit">
                  Find Destinations
                </button>
              </div>
            </form>

            <div className="preset-chips">
              <span>Quick Presets:</span>
              {[1500, 2000, 2500, 3000, 4500].map((b) => (
                <button
                  key={b}
                  type="button"
                  className={`chip-btn ${budgetInput === b ? "active" : ""}`}
                  onClick={() => {
                    setBudgetInput(b);
                    fetchBudgetSites(b);
                  }}
                >
                  Under ₹{b.toLocaleString()}
                </button>
              ))}
            </div>
          </div>

          <div className="results-container">
            <h2 className="results-title">
              Destinations Available Within ₹{budgetInput.toLocaleString()} ({budgetSites.length})
            </h2>

            {budgetLoading ? (
              <Loading message="Filtering destinations by budget..." />
            ) : budgetError ? (
              <Loading error={budgetError} onRetry={() => fetchBudgetSites(budgetInput)} />
            ) : budgetSites.length === 0 ? (
              <div className="empty-state">No destinations found within ₹{budgetInput}. Try increasing your budget limit.</div>
            ) : (
              <div className="site-cards-grid">
                {budgetSites.map((site) => (
                  <SiteCard key={site.id} site={site} />
                ))}
              </div>
            )}
          </div>
        </section>
      )}
    </div>
  );
}

export default Recommendations;
