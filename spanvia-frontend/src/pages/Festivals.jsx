import { useState, useEffect } from "react";
import api from "../services/api";
import SiteCard from "../components/SiteCard";
import Loading from "../components/Loading";

function Festivals() {
  const [data, setData] = useState({ featuredFestivals: [], sites: [] });
  const [selectedFestival, setSelectedFestival] = useState("All");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [filteredSites, setFilteredSites] = useState([]);

  const fetchFestivalData = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await api.getFestivals();
      setData(res || { featuredFestivals: [], sites: [] });
      setFilteredSites(res?.sites || []);
    } catch (err) {
      setError(err.message || "Failed to load festival data.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFestivalData();
  }, []);

  useEffect(() => {
    if (selectedFestival === "All") {
      setFilteredSites(data.sites || []);
    } else {
      api.filterSites({ keyword: selectedFestival })
        .then((sites) => setFilteredSites(sites || []))
        .catch((err) => console.error("Error filtering festival sites from Java:", err));
    }
  }, [selectedFestival, data]);

  const featuredList = data.featuredFestivals || [];
  const sitesList = data.sites || [];


  return (
    <div className="festivals-page">
      <div className="page-header text-center">
        <h1>Cultural Festival Explorer</h1>
        <p>Discover historical places celebrated during vibrant Indian heritage festivals</p>
      </div>

      {loading ? (
        <Loading message="Loading cultural festivals and heritage celebrations..." />
      ) : error ? (
        <Loading error={error} onRetry={fetchFestivalData} />
      ) : (
        <>
          <section className="featured-festivals-banner">
            <h2>Featured Heritage Festivals</h2>
            <p>Major annual festivals represented in our dataset</p>

            <div className="festival-chips">
              <button
                className={`chip-btn ${selectedFestival === "All" ? "active" : ""}`}
                onClick={() => setSelectedFestival("All")}
              >
                🎊 All Festivals ({sitesList.length})
              </button>

              {featuredList.map((fest) => (
                <button
                  key={fest}
                  className={`chip-btn ${selectedFestival === fest ? "active" : ""}`}
                  onClick={() => setSelectedFestival(fest)}
                >
                  🎉 {fest}
                </button>
              ))}
            </div>
          </section>

          <section className="festivals-content-section">
            <h2>
              {selectedFestival === "All"
                ? "All Heritage Sites Celebrating Festivals"
                : `Sites Celebrating ${selectedFestival}`}
              ({filteredSites.length})
            </h2>

            {filteredSites.length === 0 ? (
              <div className="empty-state">
                No heritage sites explicitly mapped to "{selectedFestival}".
              </div>
            ) : (
              <div className="site-cards-grid">
                {filteredSites.map((site) => (
                  <SiteCard key={site.id} site={site} />
                ))}
              </div>
            )}
          </section>
        </>
      )}
    </div>
  );
}

export default Festivals;
