import axios from "axios";

export default axios.create({
  baseURL: "https://issuetracker-backend-ks3h.onrender.com/api",
  headers: {
    "Content-Type": "application/json",
  },
});
