import { useState } from "react";

function SearchBar({ onSearch, initialValue = "", placeholder = "Search by site name, state, festival or keyword..." }) {
  const [query, setQuery] = useState(initialValue);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (onSearch) {
      onSearch(query);
    }
  };

  const handleClear = () => {
    setQuery("");
    if (onSearch) {
      onSearch("");
    }
  };

  return (
    <form className="search-bar-container" onSubmit={handleSubmit}>
      <div className="search-input-wrapper">
        <span className="search-icon">🔍</span>
        <input
          type="text"
          className="search-input"
          value={query}
          onChange={(e) => {
            setQuery(e.target.value);
            if (onSearch) onSearch(e.target.value);
          }}
          placeholder={placeholder}
        />
        {query && (
          <button type="button" className="btn-clear-search" onClick={handleClear}>
            ✖
          </button>
        )}
      </div>
      <button type="submit" className="btn-search-submit">
        Search
      </button>
    </form>
  );
}

export default SearchBar;
