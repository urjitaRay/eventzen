import { type Event } from "./Events";

export type Booking = {
  bookingId: number;

  event: Event;

  rsvpStatus: "CONFIRMED" | "PENDING" | "CANCELLED";

  bookingDate: string;
};