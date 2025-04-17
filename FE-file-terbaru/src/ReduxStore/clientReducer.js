import { createSlice } from '@reduxjs/toolkit';

const clientSlice = createSlice({
    name: 'client',
    initialState: {
        selectedClientId: null
    },
    reducers: {
        setSelectedClientId: (state, action) => {
            console.log("Client Reducer - Received ID:", action.payload);
            state.selectedClientId = action.payload;
        }
    }
});

export const { setSelectedClientId } = clientSlice.actions;
export default clientSlice.reducer;