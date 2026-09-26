import { useState, useRef, useEffect } from "react";
import api from "../services/api";

const SUGGESTED_QUESTIONS = [
  "Where is Hampi located?",
  "Why is Mahabalipuram famous?",
  "Suggest heritage places in Tamil Nadu.",
  "What are some UNESCO heritage sites in India?",
];

// Formatting helper for bot responses (renders bold text and bullet points cleanly)
function renderFormattedMessage(text) {
  if (!text) return null;
  const lines = text.split("\n");
  return lines.map((line, index) => {
    // Process bold text **text**
    const parts = line.split(/(\*\*.*?\*\*)/g);
    const formattedLine = parts.map((part, pIdx) => {
      if (part.startsWith("**") && part.endsWith("**")) {
        return <strong key={pIdx}>{part.slice(2, -2)}</strong>;
      }
      return part;
    });

    if (line.trim().startsWith("•") || line.trim().startsWith("*")) {
      return (
        <div key={index} className="chat-bullet-line">
          {formattedLine}
        </div>
      );
    }

    if (!line.trim()) {
      return <div key={index} className="chat-line-break" />;
    }

    return (
      <p key={index} className="chat-paragraph">
        {formattedLine}
      </p>
    );
  });
}

function Chatbot() {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([
    {
      sender: "bot",
      text: "Hi! I'm **SPANVIA AI**, your heritage and tourism assistant.\n\nAsk me about places, history, culture, travel, heritage sites, or anything you would like to know about your destination.",
      timestamp: new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
    },
  ]);
  const [inputMessage, setInputMessage] = useState("");
  const [isTyping, setIsTyping] = useState(false);

  const chatEndRef = useRef(null);
  const inputRef = useRef(null);

  // Auto-scroll to bottom of chat
  const scrollToBottom = () => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    if (isOpen) {
      scrollToBottom();
      inputRef.current?.focus();
    }
  }, [isOpen, messages, isTyping]);

  const toggleChat = () => {
    setIsOpen(!isOpen);
  };

  const handleSendMessage = async (textToSend) => {
    const query = (textToSend || inputMessage).trim();
    if (!query) return;

    const userTime = new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });

    // Append user message
    const userMsg = { sender: "user", text: query, timestamp: userTime };
    const newMessages = [...messages, userMsg];
    setMessages(newMessages);
    setInputMessage("");
    setIsTyping(true);

    // Format chat history for context awareness
    const historyPayload = newMessages
      .filter((m) => m.sender === "user" || m.sender === "bot")
      .map((m) => ({ role: m.sender, content: m.text }));

    try {
      const res = await api.sendChatMessage(query, historyPayload);
      const botAnswer = res?.answer || "I'm sorry, I couldn't process your request right now.";

      setMessages((prev) => [
        ...prev,
        {
          sender: "bot",
          text: botAnswer,
          timestamp: new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
        },
      ]);
    } catch (err) {
      console.warn("Chatbot API endpoint unavailable, using offline response engine:", err);
      
      // Client-side fallback engine to ensure zero crashes
      let fallbackAnswer = "I'm SPANVIA AI. You can ask me about places like Hampi, Mahabalipuram, Brihadeeswarar Temple, famous forts in Rajasthan, or UNESCO sites in India!";
      const qLower = query.toLowerCase();

      if (qLower.includes("hampi")) {
        fallbackAnswer = "**Hampi, Karnataka**\n\nHampi is a magnificent UNESCO World Heritage site and ancient capital of the Vijayanagara Empire located along the Tungabhadra River.\n\n**Famous for:**\n• Virupaksha Temple\n• Vittala Temple & Stone Chariot\n• Royal Enclosure";
      } else if (qLower.includes("mahabalipuram")) {
        fallbackAnswer = "**Mahabalipuram, Tamil Nadu**\n\nMahabalipuram is a 7th-century coastal town renowned for stone temples and monuments carved by the Pallava Dynasty.\n\n**Famous for:**\n• Shore Temple\n• Pancha Rathas\n• Arjuna's Penance (UNESCO site)";
      } else if (qLower.includes("suggest") || qLower.includes("recommend")) {
        fallbackAnswer = "**Top Recommended Heritage Destinations:**\n\n1. **Brihadeeswarar Temple** (Tamil Nadu) — UNESCO Great Living Chola Temple\n2. **Hampi** (Karnataka) — Ruins of Vijayanagara Empire\n3. **Jaisalmer Fort** (Rajasthan) — Living Desert Fort\n4. **Bhimbetka Rock Shelters** (Madhya Pradesh) — Prehistoric Cave Art";
      }

      setMessages((prev) => [
        ...prev,
        {
          sender: "bot",
          text: fallbackAnswer,
          timestamp: new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
        },
      ]);
    } finally {
      setIsTyping(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  const handleClearChat = () => {
    setMessages([
      {
        sender: "bot",
        text: "Hi! I'm **SPANVIA AI**, your heritage and tourism assistant.\n\nAsk me about places, history, culture, travel, heritage sites, or anything you would like to know about your destination.",
        timestamp: new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
      },
    ]);
  };

  return (
    <div className="spanvia-chatbot-container">
      {/* Floating Action Button */}
      <button
        className={`spanvia-chatbot-fab ${isOpen ? "active" : ""}`}
        onClick={toggleChat}
        aria-label="Toggle SPANVIA AI Chatbot"
      >
        <span className="fab-icon">{isOpen ? "✖" : "💬"}</span>
        <span className="fab-label">SPANVIA AI</span>
      </button>

      {/* Floating Chat Window Drawer */}
      {isOpen && (
        <div className="spanvia-chat-window">
          {/* Header */}
          <div className="chat-window-header">
            <div className="chat-header-info">
              <div className="bot-avatar-badge">🤖</div>
              <div>
                <h3>SPANVIA AI</h3>
                <span className="bot-status-sub">Heritage & Tourism Assistant</span>
              </div>
            </div>
            <div className="chat-header-actions">
              <button
                className="btn-chat-icon"
                onClick={handleClearChat}
                title="Clear Chat History"
              >
                🗑️
              </button>
              <button
                className="btn-chat-icon"
                onClick={toggleChat}
                title="Close Chat"
              >
                ✖
              </button>
            </div>
          </div>

          {/* Messages Body */}
          <div className="chat-window-body">
            {messages.map((msg, index) => (
              <div
                key={index}
                className={`chat-message-row ${msg.sender === "user" ? "user-row" : "bot-row"}`}
              >
                {msg.sender === "bot" && <div className="chat-avatar bot-av">🏛️</div>}
                <div className={`chat-bubble ${msg.sender === "user" ? "bubble-user" : "bubble-bot"}`}>
                  <div className="bubble-content">{renderFormattedMessage(msg.text)}</div>
                  <span className="bubble-time">{msg.timestamp}</span>
                </div>
                {msg.sender === "user" && <div className="chat-avatar user-av">👤</div>}
              </div>
            ))}

            {/* Typing Indicator */}
            {isTyping && (
              <div className="chat-message-row bot-row">
                <div className="chat-avatar bot-av">🏛️</div>
                <div className="chat-bubble bubble-bot typing-bubble">
                  <span className="dot"></span>
                  <span className="dot"></span>
                  <span className="dot"></span>
                </div>
              </div>
            )}

            {/* Suggested Question Chips (shown if only 1 message in history) */}
            {messages.length === 1 && !isTyping && (
              <div className="chat-suggestions-container">
                <p className="suggestions-label">💡 Suggested Questions:</p>
                <div className="suggestions-chips flex-wrap">
                  {SUGGESTED_QUESTIONS.map((q, idx) => (
                    <button
                      key={idx}
                      className="suggestion-chip"
                      onClick={() => handleSendMessage(q)}
                    >
                      {q}
                    </button>
                  ))}
                </div>
              </div>
            )}

            <div ref={chatEndRef} />
          </div>

          {/* Footer Input Area */}
          <div className="chat-window-footer">
            <input
              ref={inputRef}
              type="text"
              className="chat-input-field"
              placeholder="Ask about places, history, culture..."
              value={inputMessage}
              onChange={(e) => setInputMessage(e.target.value)}
              onKeyDown={handleKeyDown}
            />
            <button
              className="btn-chat-send"
              onClick={() => handleSendMessage()}
              disabled={!inputMessage.trim() || isTyping}
              title="Send Message"
            >
              ➔
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default Chatbot;
