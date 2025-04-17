import { createSlice } from '@reduxjs/toolkit';

const projectSlice = createSlice({
    name: 'project',
    initialState: {
        selectedProjectId: null,
    },
    reducers: {
        setSelectedProjectId: (state, action) => {
            console.log("Setting project ID in reducer:", action.payload);
            state.selectedProjectId = action.payload;
        },
    },
});

export const { setSelectedProjectId } = projectSlice.actions;
export default projectSlice.reducer;