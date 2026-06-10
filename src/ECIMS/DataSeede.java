package utility;

import model.*;
import repository.*;
import service.*;

/**
 * Seeds the system with realistic demo data so the application is immediately
 * usable during a demo or assignment walkthrough without manual data entry.
 */
public class DataSeeder {

    public static void seed(EmployeeRepository employeeRepo,
                            CustomerRepository customerRepo,
                            PolicyRepository   policyRepo,
                            PaymentRepository  paymentRepo,
                            ClaimRepository    claimRepo,
                            AuthService        authService) {

        // ── Staff accounts ─────────────────────────────────────────────────
        Employee admin   = EmployeeFactory.create(Role.ADMINISTRATOR, "Alice",  "Admin",
                "0700000001", "alice@nic.co.ke",   "1985-03-10", "Nairobi", "admin",   "admin123");
        Employee agent   = EmployeeFactory.create(Role.AGENT,          "Brian",  "Agent",
                "0700000002", "brian@nic.co.ke",   "1990-07-22", "Mombasa", "agent",   "agent123");
        Employee claims  = EmployeeFactory.create(Role.CLAIMS_OFFICER, "Carol",  "Claims",
                "0700000003", "carol@nic.co.ke",   "1988-11-05", "Kisumu",  "claims",  "claims123");
        Employee finance = EmployeeFactory.create(Role.FINANCE_OFFICER,"David",  "Finance",
                "0700000004", "david@nic.co.ke",   "1992-01-30", "Nakuru",  "finance", "finance123");

        employeeRepo.save(admin);
        employeeRepo.save(agent);
        employeeRepo.save(claims);
        employeeRepo.save(finance);

        // ── Seed remaining data as admin ───────────────────────────────────
        authService.login("admin", "admin123");

        CustomerService customerService = new CustomerService(customerRepo, authService);
        PolicyService   policyService   = new PolicyService(policyRepo, customerRepo, authService);
        PaymentService  paymentService  = new PaymentService(paymentRepo, policyRepo, authService);
        ClaimService    claimService    = new ClaimService(claimRepo, policyRepo, authService);

        // ── Customers ─────────────────────────────────────────────────────
        Customer c1 = customerService.createCustomer("NID001", "John",    "Mwangi",
                "0711111111", "john@email.com",    "1980-05-15", "Nairobi, Westlands");
        Customer c2 = customerService.createCustomer("NID002", "Grace",   "Achieng",
                "0722222222", "grace@email.com",   "1975-09-20", "Kisumu, Milimani");
        Customer c3 = customerService.createCustomer("NID003", "Patrick", "Otieno",
                "0733333333", "patrick@email.com", "1992-12-01", "Mombasa, Nyali");

        // ── Policies ──────────────────────────────────────────────────────
        Policy p1 = policyService.issuePolicy(c1.getCustomerId(), PolicyType.LIFE,   50000, 1000000, 12);
        Policy p2 = policyService.issuePolicy(c1.getCustomerId(), PolicyType.MOTOR,  30000,  500000, 12);
        Policy p3 = policyService.issuePolicy(c2.getCustomerId(), PolicyType.HEALTH, 40000,  750000, 12);
        Policy p4 = policyService.issuePolicy(c3.getCustomerId(), PolicyType.PROPERTY,60000, 2000000, 24);

        // ── Payments ──────────────────────────────────────────────────────
        paymentService.receivePremium(p1.getPolicyNumber(), 25000);
        paymentService.receivePremium(p2.getPolicyNumber(), 30000);
        paymentService.receivePremium(p3.getPolicyNumber(), 20000);

        // ── Claims ────────────────────────────────────────────────────────
        Claim claim1 = claimService.lodgeClaim(p3.getPolicyNumber(),
                "Hospitalisation - appendix surgery", 150000);
        Claim claim2 = claimService.lodgeClaim(p1.getPolicyNumber(),
                "Life event claim", 200000);
        claimService.approveClaim(claim1.getClaimId(), "Medical records verified");

        authService.logout();
    }
}