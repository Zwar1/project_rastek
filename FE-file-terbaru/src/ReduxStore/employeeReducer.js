import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import api from '../apiService';

// Thunk untuk mendapatkan data profil dari API setelah login
export const fetchEmployeeProfile = createAsyncThunk(
  'employee/fetchEmployeeProfile',
  async (_, { rejectWithValue }) => {
    try {
      // Ambil token dari localStorage (atau tempat penyimpanan lain)
      const token = localStorage.getItem('authToken'); // Sesuaikan dengan tempat penyimpanan token Anda

      if (!token) {
        throw new Error('Token tidak ditemukan');
      }

      const response = await api.get('api/getProfile/Information', {
        headers: {
          Authorization: `Bearer ${token}`, // Tambahkan token ke header
        },
      });

      return response.data.data; // Asumsi data ada di `data.data`
    } catch (error) {
      return rejectWithValue(error.response?.data || error.message);
    }
  }
);

const employeeSlice = createSlice({
  name: 'employee',
  initialState: {
    dumpnik: '',
    dumpEmail: '',
    profile: {}, // Menyimpan data profil
    status: 'idle',
    error: null,
    currentStep: 1,
  },
  reducers: {
    setDumpNik: (state, action) => {
      state.dumpnik = action.payload;
    },
    setEmail: (state, action) => {
      state.dumpEmail = action.payload;
    },
    setCurrentStep: (state, action) => {
      state.currentStep = action.payload; // Mengatur nilai currentStep
    },
    incrementStep: (state) => {
      if (state.currentStep < 3) {
        state.currentStep += 1; // Menambahkan currentStep
      }
    },
    decrementStep: (state) => {
      if (state.currentStep > 1) {
        state.currentStep -= 1; // Mengurangi currentStep
      }
    },
    previousStepByTwo: (state) => {
      if (state.currentStep > 2) {
        state.currentStep -= 2;
      } else {
        state.currentStep = 1;
      }
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchEmployeeProfile.fulfilled, (state, action) => {
        state.profile = action.payload;
        state.dumpnik = action.payload.employee?.nik || '';
        state.dumpEmail = action.payload.employee?.emailPribadi || '';
      })
      .addCase(fetchEmployeeProfile.rejected, (state, action) => {
        state.error = action.payload;
      });
  },
});

export const { 
  setDumpNik, 
  setEmail,
  setCurrentStep,
  incrementStep,
  decrementStep,
  previousStepByTwo,
} = employeeSlice.actions;
export default employeeSlice.reducer;
