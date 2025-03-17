import { createSlice } from "@reduxjs/toolkit";

const attendanceSlice = createSlice({
  name: "attendance",
  initialState: {
    checkInTime: null,
    checkOutTime: null,
    attendanceStatus: null,
    isCheckIn: false,
    isCheckedOutToday: false,
    LocationCheckIn: null,
  },
  reducers: {
    setCheckIn: (state, action) => {
      if (action.payload) {
        state.checkInTime = action.payload.time;
        state.attendanceStatus = action.payload.status;
        state.isCheckIn = true;
        state.isCheckedOutToday = false;
        state.LocationCheckIn = action.payload.location;
      } else {
        console.error("Payload for setCheckIn is missing");
      }
    },
    setCheckOut: (state, action) => {
      if (action.payload) {
        state.checkOutTime = action.payload.time;
        state.attendanceStatus = action.payload.status;
        state.isCheckIn = false;
        state.isCheckedOutToday = true;
        state.LocationCheckIn = action.payload.location;
      } else {
        console.error("Payload for setCheckOut is missing");
      }
    },
    resetAttendance: (state) => {
      state.checkInTime = null;
      state.checkOutTime = null;
      state.attendanceStatus = null;
      state.isCheckIn = false;
      state.isCheckedOutToday = false;
      state.LocationCheckIn = null;
    },
  },
});

export const { setCheckIn, setCheckOut, resetAttendance } = attendanceSlice.actions;
export default attendanceSlice.reducer;
