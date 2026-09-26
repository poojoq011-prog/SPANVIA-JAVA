function FilterPanel({ filters, onFilterChange, onReset, states = [], categories = [] }) {
  const handleChange = (key, value) => {
    onFilterChange({ ...filters, [key]: value });
  };

  return (
    <aside className="filter-panel">
      <div className="filter-header">
        <h3>Filter Destinations</h3>
        <button type="button" className="btn-reset-filter" onClick={onReset}>
          Reset
        </button>
      </div>

      <div className="filter-group">
        <label>State</label>
        <select
          value={filters.state || "All"}
          onChange={(e) => handleChange("state", e.target.value)}
        >
          <option value="All">All States</option>
          {states.map((st) => (
            <option key={st} value={st}>
              {st}
            </option>
          ))}
        </select>
      </div>

      <div className="filter-group">
        <label>Category</label>
        <select
          value={filters.category || "All"}
          onChange={(e) => handleChange("category", e.target.value)}
        >
          <option value="All">All Categories</option>
          {categories.map((cat) => (
            <option key={cat} value={cat}>
              {cat}
            </option>
          ))}
        </select>
      </div>

      <div className="filter-group">
        <label>
          Max Budget: <strong>₹{filters.maxBudget ? Number(filters.maxBudget).toLocaleString() : 5000}</strong>
        </label>
        <input
          type="range"
          min="1000"
          max="6000"
          step="500"
          value={filters.maxBudget || 6000}
          onChange={(e) => handleChange("maxBudget", Number(e.target.value))}
        />
      </div>

      <div className="filter-group">
        <label>Minimum Rating</label>
        <div className="rating-radio-group">
          {[0, 4.0, 4.5, 4.7].map((r) => (
            <button
              key={r}
              type="button"
              className={`chip-btn ${filters.minRating === r ? "active" : ""}`}
              onClick={() => handleChange("minRating", r)}
            >
              {r === 0 ? "Any" : `⭐ ${r}+`}
            </button>
          ))}
        </div>
      </div>

      <div className="filter-group">
        <label>UNESCO Status</label>
        <div className="unesco-toggle">
          <button
            type="button"
            className={`chip-btn ${!filters.unescoOnly ? "active" : ""}`}
            onClick={() => handleChange("unescoOnly", false)}
          >
            All Sites
          </button>
          <button
            type="button"
            className={`chip-btn ${filters.unescoOnly ? "active" : ""}`}
            onClick={() => handleChange("unescoOnly", true)}
          >
            ✨ UNESCO Only
          </button>
        </div>
      </div>

      <div className="filter-group">
        <label>Accessibility</label>
        <select
          value={filters.accessibility || "All"}
          onChange={(e) => handleChange("accessibility", e.target.value)}
        >
          <option value="All">All Access Levels</option>
          <option value="Easy">Easy</option>
          <option value="Moderate">Moderate</option>
          <option value="Challenging">Challenging</option>
        </select>
      </div>
    </aside>
  );
}

export default FilterPanel;
