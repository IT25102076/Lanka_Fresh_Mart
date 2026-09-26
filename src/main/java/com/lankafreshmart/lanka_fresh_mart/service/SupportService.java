package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.SupportTicket;
import com.lankafreshmart.lanka_fresh_mart.model.User;
import com.lankafreshmart.lanka_fresh_mart.repository.SupportTicketRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportTicketRepository ticketRepository;
    private final UserRepository userRepository;

    // CREATE
    @Transactional
    public SupportTicket createTicket(String email, String subject, String phoneNumber, String message) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        SupportTicket ticket = new SupportTicket();
        ticket.setCustomer(customer);
        ticket.setSubject(subject);
        ticket.setPhoneNumber(phoneNumber);
        ticket.setMessage(message);
        ticket.setStatus(SupportTicket.Status.OPEN);
        
        return ticketRepository.save(ticket);
    }

    // READ (Customer)
    public List<SupportTicket> getTicketsForCustomer(String email) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ticketRepository.findByCustomerOrderByCreatedAtDesc(customer);
    }

    // READ (Admin)
    public List<SupportTicket> getAllTickets() {
        return ticketRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public SupportTicket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    // UPDATE
    @Transactional
    public void replyToTicket(Long id, String reply, SupportTicket.Status status) {
        SupportTicket ticket = getTicketById(id);
        ticket.setAdminReply(reply);
        ticket.setStatus(status);
        ticketRepository.save(ticket);
    }

    // DELETE
    @Transactional
    public void deleteTicket(Long id, String requestedByEmail, boolean isAdmin) {
        SupportTicket ticket = getTicketById(id);
        
        // Admins can only delete if the status is RESOLVED
        if (isAdmin && ticket.getStatus() != SupportTicket.Status.RESOLVED) {
            throw new RuntimeException("Admins can only delete tickets that have been RESOLVED.");
        }
        
        // Customers can delete if they own the ticket and it is not RESOLVED
        if (!isAdmin) {
            if (!ticket.getCustomer().getEmail().equals(requestedByEmail)) {
                throw new RuntimeException("You are not authorized to delete this ticket.");
            }
            if (ticket.getStatus() == SupportTicket.Status.RESOLVED) {
                throw new RuntimeException("You cannot delete a ticket after it has been RESOLVED.");
            }
        }
        
        ticketRepository.delete(ticket);
    }
}
