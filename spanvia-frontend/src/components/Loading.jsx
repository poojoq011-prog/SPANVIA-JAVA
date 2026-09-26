function Loading({ message = "Loading SPANVIA heritage dataset...", error = null, onRetry = null }) {
  if (error) {
    return (
      <div className="error-banner-container">
        <div className="error-card">
          <div className="error-icon">⚠️</div>
          <h3>Connection Issue</h3>
          <p className="error-message">{error}</p>
          <p className="error-subtext">
            Check that the Core Java REST server (`SpanviaServer`) is running on port 8080.
          </p>
          {onRetry && (
            <button className="btn-retry" onClick={onRetry}>
              🔄 Retry Connection
            </button>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className="loading-container">
      <div className="spinner"></div>
      <p className="loading-text">{message}</p>
    </div>
  );
}

export default Loading;
