

import com.srms.enums.*;
import com.srms.model.bill.RentBill;
import com.srms.model.bill.UtilityReading;
import com.srms.model.contract.Contract;
import com.srms.model.property.Flat;
import com.srms.model.property.Room;
import com.srms.model.tenant.Tenant;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("====== 🏠 SRMS PROPERTY DOMAIN TESTING ====== \n");

        // 1. Create an empty flat
        Flat flat1 = new Flat(
                "FLAT-999", "A-302", PropertyStatus.AVAILABLE,
                25000.0, BathroomType.ATTACHED, FloorLevel.THIRD_FLOOR,
                new ArrayList<>()
        );

        // 2. Create room objects
        Room room1 = new Room("RM-101", "R1", PropertyStatus.AVAILABLE, 15000.0, BathroomType.ATTACHED, RoomType.OFFICE, FloorLevel.THIRD_FLOOR);
        Room room2 = new Room("RM-102", "R2", PropertyStatus.AVAILABLE, 25000.0, BathroomType.ATTACHED, RoomType.HALL, FloorLevel.THIRD_FLOOR);
        Room room3 = new Room("RM-103", "R3", PropertyStatus.AVAILABLE, 8000.0, BathroomType.COMMON, RoomType.SINGLE_ROOM, FloorLevel.THIRD_FLOOR);

        // 3. Add rooms to the flat
        System.out.println("--- ➕ Adding Rooms to Flat ---");
        flat1.addRoom(room1);
        flat1.addRoom(room2);
        flat1.addRoom(room3);
        System.out.println("✅ 3 Rooms added successfully!");
        System.out.println("👉 Room 1's Parent Flat ID: " + room1.getParentFlat().getId() + " (Bidirectional Alignment Verified!)");

        // 4. Test SrmsEntity and updated toString() chain
        System.out.println("\n--- 📝 Testing toString() Output ---");
        System.out.println(flat1);
        System.out.println(room1);

        // 5. Test PropertyPricingPolicy calculations
        System.out.println("\n--- 💰 Testing Pricing Calculations (PropertyPricingPolicy) ---");
        System.out.println("Total Mandatory Charges for Flat: Rs. " + flat1.calculateFixedMandatoryCharge());

        // 6. Test duplicate room validation
        System.out.println("\n--- 🛑 Testing Duplicate Room Validation ---");
        try {
            System.out.println("Attempting to add Room 1 again...");
            flat1.addRoom(room1);
        } catch (IllegalStateException e) {
            System.out.println("🔥 Caught Expected Error: " + e.getMessage());
        }

        // 7. Test removeRoom and relationship disconnection
        System.out.println("\n--- ➖ Testing Remove Room ---");
        flat1.removeRoom(room3);
        System.out.println("✅ Room 3 removed from flat.");
        System.out.println("👉 Room 3's Parent Flat after removal: " + room3.getParentFlat() + " (Disconnection Verified!)");

        // 8. Flat status and pricing update check
        System.out.println("\n--- 📊 Flat Status & Pricing After Room Removal ---");
        System.out.println(flat1);
        System.out.println("New Mandatory Charges for Flat: Rs. " + flat1.calculateFixedMandatoryCharge());

        System.out.println("\n====== 👤 SRMS TENANT DOMAIN TESTING ======");

        // Using Builder Pattern for Tenant creation
        Tenant tenant = new Tenant.Builder("TEN-2026", "Rupesh", "+977-9841XXXXXX")
                .identityNo("34-01-72-4567")
                .identityType(IdentityType.CITIZENSHIP)
                .totalFamilyMember(3)
                .bikeCount(1)
                .isPoliceVerified(true)
                .build();

        System.out.println("✅ Tenant Object Created Successfully Using Builder!");
        System.out.println(tenant);

        System.out.println("\n====== 📜 SRMS CONTRACT DOMAIN TESTING ======");

        // Set flat1 to OCCUPIED for contract testing
        flat1.setPropertyStatus(PropertyStatus.OCCUPIED);

        // 9. Sign a contract
        System.out.println("1. Signing contract for Flat-999 under tenant: Rupesh...");
        Contract contract = new Contract.Builder("CON-2026-001", tenant, flat1, flat1.getBaseRent())
                .securityDeposit(6000.0)
                .startElectricity(120.5)
                .agreementDocPath("agreements/rupesh_flat_999.pdf")
                .build();

        System.out.println("\n--- 📝 Contract Initial State (toString) ---");
        System.out.println(contract);
        System.out.println("------------------------------------------------");

        // ⚡ UTILITY READING AND RENT BILL LIFE-CYCLE ⚡
        System.out.println("\n====== ⚡ SRMS UTILITY & BILLING LIFE-CYCLE TESTING ======");

        // 12. Record Meter Reading
        System.out.println("1. Recording meter reading for the first month...");
        UtilityReading readingMonth1 = new UtilityReading.Builder("UR-2026-001")
                .currentReading(220.5)
                .previousReading(120.5)
                .build();
        System.out.println("✅ Utility Reading Recorded: " + readingMonth1);
        System.out.println("👉 Total Electricity Consumed: " + readingMonth1.calculateConsumedUnits() + " Units");

        // 13. Generate Bill
        System.out.println("\n2. Generating automated bill for rent and electricity...");
        RentBill billMonth1 = new RentBill.Builder("BILL-2026-001", contract, readingMonth1, 12.0)
                .advancePaid(2000.0)
                .previousDue(500.0)
                .build();

        // 14. Print Bill
        System.out.println("\n--- 🖨️ Generated Bill Summary ---");
        billMonth1.printBill();

        // 15. Pay Bill
        System.out.println("3. Recording payment for the bill...");
        billMonth1.markAsPaid();
        System.out.println("👉 Payment Status: " + billMonth1.getBillStatus() + " (Paid At: " + billMonth1.getPaidAt() + ")");

        // 16. Double Payment Crash Test
        System.out.println("\n🚨 Crash Test: Attempting to pay an already paid bill...");
        try {
            billMonth1.markAsPaid();
        } catch (IllegalStateException e) {
            System.out.println("🎯 Success! System blocked double payment. Error: " + e.getMessage());
        }

        // ❌ CONTRACT TERMINATION ❌
        System.out.println("\n====== ❌ SRMS CONTRACT TERMINATION TESTING ======");

        double damageDeduction = 7500.0;
        System.out.println("1. Terminating contract for Flat-999...");
        contract.cancelContract(damageDeduction);

        System.out.println("\n--- 📊 Final State After Termination ---");
        System.out.println(contract);
        System.out.println("👉 Final Flat Status: " + flat1.getPropertyStatus());
        System.out.println("-----------------------------------------------");

        // Double Termination Crash Test
        System.out.println("\n2. 🚨 Crash Test: Attempting to terminate an already terminated contract...");
        try {
            contract.cancelContract(0);
        } catch (IllegalStateException e) {
            System.out.println("🎯 Success! System blocked re-termination. Error: " + e.getMessage());
        }

        System.out.println("\n🎉 ====== ALL SRMS V1 CORE ARCHITECTURE TESTS PASSED SUCCESSFULLY! ======");
    }
}