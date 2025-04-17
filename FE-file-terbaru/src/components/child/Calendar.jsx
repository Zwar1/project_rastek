import React, { useEffect, useRef, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { INITIAL_EVENTS, createEventId } from "../../hook/event-utils.js";
import { Modal } from "bootstrap/dist/js/bootstrap.bundle.min.js";
import useCompanyCalendar from "../../hook/useCompanyCalendar.js";
import useEmployeeCalendar from "../../hook/useEmployeeCalendar.js";
import { info } from "sass";
import { a } from "@react-spring/web";
import { Icon } from "@iconify/react/dist/iconify.js";
import * as bootstrap from 'bootstrap';

const Calendar = ({ events = [], employeeEvents = [], loading = false, employeeLoading = false, refreshEvents, onDateSelected,
  deleteCompanyCalendar, refreshEmployeeEvents, deleteEmployeeCalendar }) => {
  const [selectedDate, setSelectedDate] = useState(null);
  const [eventTitle, setEventTitle] = useState(null);
  const [selectedEventId, setSelectedEventId] = useState(null);
  //const { CalendarEvents, loading, refreshEvents } = useCalendar();
  const calendarRef = useRef(null);
  const today = new Date();
  today.setHours(0, 0, 0, 0); // Normalize to beginning of day
  //const { loading: employeeLoading, error: employeeError } = useEmployeeCalendar()

  const handleDateSelect = (selectInfo) => {
    setSelectedDate(selectInfo);
    setEventTitle("");

    const modalElement = document.getElementById("cutiModal");
    if (modalElement) {
      const modalInstance = new Modal(modalElement);

      const currentEmployeeEvents = [...employeeEvents];

      modalInstance.show();

      modalElement.addEventListener('hidden.bs.modal', async () => {
        if (refreshEvents) {
          await refreshEvents();
        }
        if (refreshEmployeeEvents) {
          await refreshEmployeeEvents();
        }
      }, { once: true });
    }
  };


  const handleSaveEvent = () => {
    if (!selectedDate || !eventTitle) return;

    const calendarApi = selectedDate.view.calendar;
    calendarApi.addEvent({
      id: createEventId(),
      title: eventTitle,
      start: selectedDate.startStr,
      end: selectedDate.endStr,
      allDay: selectedDate.allDay,
    });

    setSelectedDate(null);
    setEventTitle("");
    const modalElement = document.getElementById("exampleModal");
    if (modalElement) {
      const modalInstance = Modal.getInstance(modalElement);
      modalInstance.hide();
    }
  };

  /* eslint-disable-next-line no-restricted-globals */
  const handleEventClick = async (clickInfo) => {
    const eventId = clickInfo.event.id;
    //const fullEvent = employeeEvents.event.idCalendar;

    console.log('Event clicked:', eventId);

    const fullEvent = [...events, ...(employeeEvents || [])].find(
      event => (event.idCalendar || event.id).toString() === eventId.toString()
    );

    if (!fullEvent) {
      console.warn('Event not found:', eventId);
      return;
    }

    const isEmployeeEvent = !events.some(e => (e.idCalendar || e.id).toString() === eventId.toString());

    setSelectedEventId({
      id: fullEvent.id || fullEvent.idCalendar,
      idEvent: fullEvent.idEvent || fullEvent.idEmployeeEvent,
      event: clickInfo.event,
      isEmployeeEvent: isEmployeeEvent,
      nik: fullEvent.nik
    });

    const modalElement = document.getElementById('deleteEventModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  };
  /* eslint-enable no-restricted-globals */

  const allEvents = [...events, ...(employeeEvents || [])].map((event) => {
    try {
      if (!event.startDate || !event.endDate) {
        console.warn('Missing date for event:', event);
        return null;
      }

      const startDate = new Date(event.startDate);
      let endDate = new Date(event.endDate);

      if (isNaN(startDate.getTime()) || isNaN(endDate.getTime())) {
        console.warn('Invalid date format for event:', event);
        return null;
      }

      endDate.setDate(endDate.getDate() + 1);
      endDate.setHours(23, 59, 59);

      const isEmployeeEvent = Boolean(event.idEmployeeCalendar || event.isCuti !== undefined);
      const isHoliday = isEmployeeEvent ? event.isCuti : event.isFree;

      console.log('Processing event:', {
        id: event.id,
        title: event.eventName,
        isEmployeeEvent,
        isHoliday,
        source: isEmployeeEvent ? 'employee' : 'company'
      });

      return {
        id: String(event.id || event.idCalendar), // Ensure ID is a string for consistency
        title: event.eventName || event.title || (isEmployeeEvent ? 'Leave' : 'Event'),
        start: startDate.toISOString(),
        end: endDate.toISOString(),
        display: 'block',
        allDay: true,
        description: event.description || '',
        backgroundColor: isHoliday ? '#dc3545' : '#0d6efd',
        borderColor: isHoliday ? '#dc3545' : '#0d6efd',
        extendedProps: {
          isHoliday: isHoliday,
          isEmployeeEvent: isEmployeeEvent,
          description: event.description || '',
          eventSource: isEmployeeEvent ? 'employee' : 'company',
          originalId: event.id || event.idCalendar
        }
      };
    } catch (error) {
      console.error('Error formatting event:', error);
      return null;
    }
  }).filter(event => event !== null);

  const isDateInRange = (dateStr, event) => {
    const currentDate = new Date(dateStr);
    const startDate = new Date(event.start);
    const endDate = new Date(event.end);

    // Reset hours to compare dates only
    currentDate.setHours(0, 0, 0, 0);
    startDate.setHours(0, 0, 0, 0);
    endDate.setHours(0, 0, 0, 0);

    return currentDate >= startDate && currentDate <= endDate;
  };

  useEffect(() => {
    if (!loading && !employeeLoading && calendarRef.current) {
      const calendarApi = calendarRef.current.getApi();
      calendarApi.removeAllEvents();
      allEvents.forEach(event => {
        calendarApi.addEvent(event);
      });
      calendarApi.render();
    }
  }, [events, employeeEvents, loading, employeeLoading]);

  useEffect(() => {
    const initializeCalendar = async () => {
      if (refreshEvents) {
        await refreshEvents();
      }
      if (refreshEmployeeEvents) {
        await refreshEmployeeEvents();
      }
    };
    initializeCalendar();
  }, []);

  useEffect(() => {
    const handleModalHidden = async () => {
      console.log('Modal hidden, refreshing events...');
      if (refreshEvents) {
        console.log('Refreshing company events...');
        await refreshEvents();
      }
      if (refreshEmployeeEvents) {
        console.log('Refreshing employee events...');
        await refreshEmployeeEvents();
      }
    };

    const modalIds = ['deleteEventModal', 'cutiModal'];
    const cleanupFunctions = [];

    modalIds.forEach(id => {
      const modal = document.getElementById(id);
      if (modal) {
        modal.addEventListener('hidden.bs.modal', handleModalHidden);
        cleanupFunctions.push(() => {
          modal.removeEventListener('hidden.bs.modal', handleModalHidden);
        });
      }
    });

    return () => {
      cleanupFunctions.forEach(cleanup => cleanup());
    };
  }, [refreshEvents, refreshEmployeeEvents]);

  useEffect(() => {
    console.log('Employee Events:', employeeEvents);
    console.log('Company Events:', events);
    console.log('Combined Events:', allEvents);

    if (!loading && !employeeLoading && calendarRef.current) {
      const calendarApi = calendarRef.current.getApi();
      calendarApi.removeAllEvents();
      allEvents.forEach(event => {
        console.log('Adding event to calendar:', event.title);
        calendarApi.addEvent(event);
      });
      calendarApi.render();
    }
  }, [events, employeeEvents, loading, employeeLoading]);

  return (
    <div className="demo-app">
      <div className="demo-app-main">
        <FullCalendar
          plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
          headerToolbar={{
            left: "title",
            right: "prev,next today",
          }}
          buttonText={{ today: "Today" }}
          initialView="dayGridMonth"
          dateClick={(info) => {
            onDateSelected(info.date);
          }}
          editable={true}
          selectable={true}
          selectMirror={true}
          dayMaxEvents={true}
          weekends={true}
          events={allEvents}
          eventTimeFormat={{
            hour: '2-digit',
            minute: '2-digit',
            meridiem: 'short'
          }}

          selectAllow={(selectInfo) => {
            const today = new Date();
            today.setHours(0, 0, 0, 0);
            return selectInfo.start >= today;
          }}
          select={handleDateSelect}
          eventContent={renderEventContent}
          eventClick={handleEventClick}
          firstDay={1}
          eventDidMount={(info) => {
            const eventStart = new Date(info.event.start);
            const eventEnd = new Date(info.event.end);

            // Normalize dates for comparison
            const startDay = new Date(eventStart.getFullYear(), eventStart.getMonth(), eventStart.getDate());
            const endDay = new Date(eventEnd.getFullYear(), eventEnd.getMonth(), eventEnd.getDate());

            // Calculate duration in days
            const duration = Math.round((endDay - startDay) / (1000 * 60 * 60 * 24));

            if (duration > 0) {
              // Style for multi-day events
              info.el.style.display = 'block';
              info.el.style.margin = '1px 0';
              info.el.style.padding = '2px 5px';

              // Force event to take full width
              const fcEventMain = info.el.querySelector('.fc-event-main');
              if (fcEventMain) {
                fcEventMain.style.maxWidth = '100%';
                fcEventMain.style.width = '100%';
              }

              // Set minimum height for day cells
              const dayCell = info.el.closest('.fc-daygrid-day');
              if (dayCell) {
                dayCell.style.minHeight = '120px';
              }

              // Fix event container height
              const eventContainer = info.el.closest('.fc-daygrid-event-harness');
              if (eventContainer) {
                eventContainer.style.height = 'auto';
              }
            }
          }}
          dayMaxEventRows={3}
          moreLinkText={count => `+${count} more`}
          moreLinkClick="popover"
          dayCellClassNames={(arg) => {
            const cellDate = new Date(arg.date);
            cellDate.setHours(0, 0, 0, 0);

            const isHoliday = allEvents.some((event) => {
              const eventIsHoliday = event.extendedProps &&
                event.extendedProps.isHoliday;

              if (!eventIsHoliday) return false;

              const eventStart = new Date(event.start);
              const eventEnd = new Date(event.end);
              eventStart.setHours(0, 0, 0, 0);
              eventEnd.setDate(eventEnd.getDate() - 1);
              eventEnd.setHours(23, 59, 59, 0);

              return cellDate >= eventStart && cellDate <= eventEnd;
            });

            return isHoliday ? "holiday-cell holiday-date-black" : "";
          }}
        />
      </div>
      <div
        className="modal fade"
        id="deleteEventModal"
        tabIndex={-1}
        aria-hidden="true"
      >
        <div className="modal-dialog modal-sm modal-dialog modal-dialog-centered">
          <div className="modal-content radius-16 bg-base">
            <div className="modal-body p-24 text-center">
              <span className="mb-16 fs-1 line-height-1 text-danger">
                <Icon icon="fluent:delete-24-regular" className="menu-icon" />
              </span>
              <h6 className="text-lg fw-semibold text-primary-light mb-0">
                Are you sure you want to delete this event?
              </h6>
              <div className="d-flex align-items-center justify-content-center gap-3 mt-24">
                <button
                  type="reset"
                  className="w-50 border border-danger-600 bg-hover-danger-200 text-danger-600 text-md px-10 py-11 radius-8"
                  data-bs-dismiss="modal"
                >
                  Cancel
                </button>
                <button
                  type="button"
                  className="w-50 btn btn-primary border border-primary-600 text-md px-24 py-12 radius-8"
                  onClick={async () => {
                    try {
                      console.log('Deleting event:', selectedEventId);

                      if (selectedEventId.isEmployeeEvent) {
                        await deleteEmployeeCalendar(selectedEventId.id);
                        console.log('Employee event deleted successfully');
                      } else {
                        await deleteCompanyCalendar(selectedEventId.id);
                        console.log('Company event deleted successfully');
                      }

                      selectedEventId.event.remove();

                      if (refreshEvents) {
                        console.log('Refreshing company events...');
                        await refreshEvents();
                      }
                      if (refreshEmployeeEvents) {
                        console.log('Refreshing employee events...');
                        await refreshEmployeeEvents();
                      }

                      alert('Event deleted successfully');
                    } catch (error) {
                      console.error('Error deleting event:', error);
                      alert(error.message || 'Failed to delete event');
                    }

                    const modal = document.getElementById('deleteEventModal');
                    const bsModal = bootstrap.Modal.getInstance(modal);
                    if (bsModal) {
                      bsModal.hide();
                    }
                  }}
                  data-bs-dismiss="modal"
                >
                  Delete
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

const renderEventContent = (eventInfo) => {
  //console.log('Event Info:', eventInfo);
  const isHoliday = eventInfo.event.extendedProps.isHoliday;

  const eventStyle = {
    whiteSpace: 'normal',
    display: 'block',
    padding: '2px 5px',
    backgroundColor: isHoliday ? 'transparent' : eventInfo.event.backgroundColor,
    color: isHoliday ? '#dc3545' : '#fff',
    border: isHoliday ? 'none' : '1px solid',
    borderColor: eventInfo.event.borderColor,
    boxShadow: isHoliday ? 'none' : '0 2px 4px rgba(0,0,0,0.1)'
  };

  return (
    <div style={eventStyle}>
      <span style={{ fontWeight: isHoliday ? 'normal' : 'bold' }}>
        {eventInfo.event.title}
      </span>
      {eventInfo.event.extendedProps.description && (
        <small style={{
          display: 'block',
          fontSize: '0.85em',
          color: isHoliday ? '#dc3545' : 'inherit'
        }}>
          {eventInfo.event.extendedProps.description}
        </small>
      )}
    </div>
  );
};

export default Calendar;
