import React, { useEffect, useRef, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { INITIAL_EVENTS, createEventId } from "../../hook/event-utils.js";
import { Modal } from "bootstrap/dist/js/bootstrap.bundle.min.js";
import useCalendar from "../../hook/useCalendar.js";
import { info } from "sass";
import { a } from "@react-spring/web";
import { Icon } from "@iconify/react/dist/iconify.js";
import * as bootstrap from 'bootstrap';

const Calendar = ({ events = [], loading = false, refreshEvents, onDateSelected, deleteCompanyCalendar }) => {
  const [selectedDate, setSelectedDate] = useState(null);
  const [eventTitle, setEventTitle] = useState(null);
  const [selectedEventId, setSelectedEventId] = useState(null);
  //const { CalendarEvents, loading, refreshEvents } = useCalendar();
  const calendarRef = useRef(null);
  const today = new Date();
  today.setHours(0, 0, 0, 0); // Normalize to beginning of day

  const handleDateSelect = (selectInfo) => {
    setSelectedDate(selectInfo);
    setEventTitle("");

    const modalElement = document.getElementById("cutiModal");
    if (modalElement) {
      const modalInstance = new Modal(modalElement);
      modalInstance.show();
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
    const fullEvent = events.find(event => event.id.toString() === eventId.toString());

    if (!fullEvent) {
      alert('Event not found');
      return;
    }

    setSelectedEventId({
      id: fullEvent.id,
      idEvent: fullEvent.idEvent,
      event: clickInfo.event
    });

    const modalElement = document.getElementById('deleteEventModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  };
  /* eslint-enable no-restricted-globals */

  const formattedEvents = events.map((event) => {
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

      // Add one day to end date for proper rendering (exclusive end date in FullCalendar)
      endDate.setDate(endDate.getDate() + 1);

      return {
        id: event.idCalendar || event.id,
        title: event.eventName,
        start: startDate.toISOString(),
        isFree: event.isFree,
        end: endDate.toISOString(),
        allDay: true,
        description: event.description,
        backgroundColor: event.isFree ? '#dc3545' : '#0d6efd',
        borderColor: event.isFree ? '#dc3545' : '#0d6efd'
      };
    } catch (error) {
      console.error('Error formatting event:', event, error);
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
    if (!loading && calendarRef.current) {
      const calendarApi = calendarRef.current.getApi();
      calendarApi.removeAllEvents();

      // Re-add all events from source
      formattedEvents.forEach(event => {
        calendarApi.addEvent(event);
      });

      calendarApi.render();
    }
  }, [events, loading]);

  useEffect(() => {
    const initializeCalendar = async () => {
      if (refreshEvents) {
        await refreshEvents();
      }
    };
    initializeCalendar();
  }, []);

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
          events={formattedEvents}
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
            setTimeout(() => {
              const eventStart = new Date(info.event.start);
              eventStart.setHours(0, 0, 0, 0);

              const allEvents = info.view.calendar.getEvents();
              const overlappingEvents = allEvents.filter((e) => {
                if (!e.start) return false;
                const eStart = new Date(e.start);
                eStart.setHours(0, 0, 0, 0);
                return eStart.getTime() === eventStart.getTime();
              });

              const dayCell = info.el.closest('.fc-daygrid-day');
              if (dayCell) {
                dayCell.style.minHeight = '120px';
              }

              if (overlappingEvents.length === 1) {
                // For single events, ensure proper display
                info.el.style.display = 'block';
                info.el.style.maxHeight = '80px';
                info.el.style.overflow = 'hidden';

                const parentCell = info.el.closest('.fc-daygrid-day-events');
                if (parentCell) {
                  parentCell.style.maxHeight = '90px';
                  parentCell.style.overflow = 'hidden';
                }
              }

              // if (overlappingEvents.length === 1) {
              //   info.el.style.display = 'block';
              //   info.el.style.maxHeight = '100px';

                // const parentCell = info.el.closest('.fc-daygrid-day-events');
                // if (parentCell) {
                //   parentCell.style.maxHeight = '100px';
                //   parentCell.style.position = 'relative';
                // }
              //}
            }, 0);
          }}
          dayCellClassNames={(arg) => {
            const cellDate = new Date(arg.date);
            cellDate.setHours(0, 0, 0, 0);

            const isHoliday = formattedEvents.some((event) => {
              if (!event.isFree) return false;

              const eventStart = new Date(event.start);
              const eventEnd = new Date(event.end);
              eventStart.setHours(0, 0, 0, 0);
              eventEnd.setHours(0, 0, 0, 0);

              return cellDate >= eventStart && cellDate < eventEnd;
            });
            return isHoliday ? "holiday-cell" : "";
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
                      await deleteCompanyCalendar(selectedEventId.id, {
                        idEvent: selectedEventId.idEvent
                      });
                      selectedEventId.event.remove();
                      if (refreshEvents) {
                        await refreshEvents();
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
  return (
    <span style={{ whiteSpace: 'normal', display: 'block' }}>
      {eventInfo.timeText}
      {eventInfo.event.title}
    </span>
  );
};

export default Calendar;
