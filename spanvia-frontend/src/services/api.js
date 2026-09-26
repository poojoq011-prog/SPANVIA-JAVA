const API_BASE_URL = "http://localhost:8080/api";

async function fetchJson(endpoint, options = {}) {
  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
      ...options,
    });

    if (!response.ok) {
      const errorText = await response.text();
      try {
        const errorJson = JSON.parse(errorText);
        throw new Error(errorJson.message || errorJson.error || `HTTP error ${response.status}`);
      } catch (e) {
        throw new Error(`HTTP error ${response.status}`);
      }
    }

    return await response.json();
  } catch (error) {
    if (error.name === "TypeError" && error.message.includes("fetch")) {
      throw new Error("Unable to connect to SPANVIA server. Please make sure the backend is running on http://localhost:8080.");
    }
    throw error;
  }
}

export const api = {
  async getAllSites() {
    return await fetchJson("/sites");
  },

  async filterSites(params = {}) {
    const query = new URLSearchParams();
    if (params.keyword) query.append("keyword", params.keyword);
    if (params.state) query.append("state", params.state);
    if (params.category) query.append("category", params.category);
    if (params.maxBudget) query.append("maxBudget", params.maxBudget);
    if (params.minRating) query.append("minRating", params.minRating);
    if (params.unescoOnly) query.append("unescoOnly", "true");
    if (params.accessibility) query.append("accessibility", params.accessibility);
    if (params.sortBy) query.append("sortBy", params.sortBy);
    return await fetchJson(`/sites/filter?${query.toString()}`);
  },


  async getSiteById(id) {
    return await fetchJson(`/sites/${id}`);
  },

  async searchSites(keyword) {
    return await fetchJson(`/sites/search?keyword=${encodeURIComponent(keyword)}`);
  },

  async getSitesByState(state) {
    return await fetchJson(`/sites/state/${encodeURIComponent(state)}`);
  },

  async getBudgetRecommendations(maxBudget) {
    return await fetchJson(`/sites/budget?max=${maxBudget}`);
  },

  async getFestivals() {
    return await fetchJson("/sites/festivals");
  },

  async getUnescoSites() {
    return await fetchJson("/sites/unesco");
  },

  async getStats() {
    return await fetchJson("/sites/stats");
  },

  async getAiRecommendations(preferences) {
    return await fetchJson("/sites/recommend", {
      method: "POST",
      body: JSON.stringify(preferences),
    });
  },

  async login(email, password) {
    return await fetchJson("/auth/login", {
      method: "POST",
      body: JSON.stringify({ email, password }),
    });
  },

  async sendChatMessage(message, history = []) {
    return await fetchJson("/chat", {
      method: "POST",
      body: JSON.stringify({ message, history }),
    });
  },
};

export default api;
