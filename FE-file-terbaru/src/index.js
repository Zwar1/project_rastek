import React from "react";
import ReactDOM from "react-dom/client";
import "react-quill/dist/quill.snow.css";
import "jsvectormap/dist/css/jsvectormap.css";
import "react-toastify/dist/ReactToastify.css";
import "react-modal-video/css/modal-video.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
// Redux Component
import store, { persistor } from "./store";
import { Provider } from "react-redux"; // Import Provider from react-redux
import { NotificationProvider } from "./Notifications/NotificationContext";
import { PersistGate } from 'redux-persist/integration/react';

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <NotificationProvider>
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
        {" "}
        <App />
      </PersistGate>
    </Provider>
  </NotificationProvider>
);

reportWebVitals();
