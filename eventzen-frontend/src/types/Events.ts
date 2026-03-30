export type Event = {
  eventId: number;
  eventName: string;
  eventType: string;
  eventDate: string;   // LocalDate → string (ISO format)
  eventTime: string;   // LocalTime → string
  venue: string;
  description: string;
  budget: number;

  createdBy?: {
    id: number;
    name: string;
    email: string;
  };
};