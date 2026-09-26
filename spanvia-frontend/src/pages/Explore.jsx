import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import api from "../services/api";
import SiteCard from "../components/SiteCard";
import SearchBar from "../components/SearchBar";
import FilterPanel from "../components/FilterPanel";
import Loading from "../components/Loading";

function Explore() {
  const [searchParams] = useSearchParams();
  const initialQuery = searchParams.get("q") || "";

  const [allMasterSites, setAllMasterSites] = useState([]);
  const [displayedSites, setDisplayedSites] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchQuery, setSearchQuery] = useState(initialQuery);

  const [filters, setFilters] = useState({
    state: "All",
    category: "All",
    maxBudget: 6000,
    minRating: 0,
    unescoOnly: false,
    accessibility: "All",
  });

  const [sortBy, setSortBy] = useState("rating-desc");

  // Load master sites list once to populate filter options
  useEffect(() => {
    api.getAllSites()
      .then((data) => setAllMasterSites(data || []))
      .catch((err) => console.error("Error loading site list:", err));
  }, []);

  // Delegate search, filtering, and sorting to Java Backend
  const fetchFilteredSitesFromJava = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.filterSites({
        keyword: searchQuery,
        state: filters.state,
        category: filters.category,
        maxBudget: filters.maxBudget,
        minRating: filters.minRating,
        unescoOnly: filters.unescoOnly,
        accessibility: filters.accessibility,
        sortBy: sortBy,
      });
      setDisplayedSites(data || []);
    } catch (err) {
      setError(err.message || "Failed to load heritage dataset from Java server.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFilteredSitesFromJava();
  }, [searchQuery, filters, sortBy]);

  // Compute unique states and categories for filter dropdowns from master dataset
  const availableStates = Array.from(new Set(allMasterSites.map((s) => s.state))).sort();
  const availableCategories = Array.from(new Set(allMasterSites.map((s) => s.category))).sort();

  const handleResetFilters = () => {
    setSearchQuery("");
    setFilters({
      state: "All",
      category: "All",
      maxBudget: 6000,
      minRating: 0,
      unescoOnly: false,
      accessibility: "All",
    });
    setSortBy("rating-desc");
  };


  return (
    <div className="explore-page">
      <div className="page-header">
        <h1>Explore Indian Heritage Sites</h1>
        <p>Search and filter through 100+ monuments loaded dynamically from Java backend</p>
      </div>

      <div className="explore-search-section">
        <SearchBar
          initialValue={searchQuery}
          onSearch={(q) => setSearchQuery(q)}
          placeholder="Search by site name, state (e.g. Gujarat, Odisha), or category..."
        />
      </div>

      {loading ? (
        <Loading message="Loading heritage dataset from Java backend..." />
      ) : error ? (
        <Loading error={error} onRetry={fetchFilteredSitesFromJava} />
      ) : (
        <div className="explore-layout">
          <FilterPanel
            filters={filters}
            onFilterChange={setFilters}
            onReset={handleResetFilters}
            states={availableStates}
            categories={availableCategories}
          />

          <main className="explore-main">
            <div className="toolbar flex-header">
              <div className="results-count">
                Showing <strong>{displayedSites.length}</strong> of {allMasterSites.length || 104} heritage places
              </div>

              <div className="sort-wrapper">
                <label>Sort By: </label>
                <select value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
                  <option value="rating-desc">Highest Rated ⭐</option>
                  <option value="budget-asc">Budget: Low to High 💵</option>
                  <option value="budget-desc">Budget: High to Low 💰</option>
                  <option value="name-asc">Name: A to Z 🔤</option>
                </select>
              </div>
            </div>

            {displayedSites.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">🔍</div>
                <h3>No Heritage Sites Match Your Search</h3>
                <p>Try adjusting your search keyword or clearing the filters.</p>
                <button className="btn-reset-filter" onClick={handleResetFilters}>
                  Clear All Filters
                </button>
              </div>
            ) : (
              <div className="site-cards-grid">
                {displayedSites.map((site) => (
                  <SiteCard key={site.id} site={site} />
                ))}
              </div>
            )}
          </main>
        </div>

      )}
    </div>
  );
}

export default Explore;
