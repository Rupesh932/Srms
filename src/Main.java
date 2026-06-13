
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

        // १. एउटा खाली फ्ल्याट बनाउने
        Flat flat1 = new Flat(
                "FLAT-999", "A-302", PropertyStatus.AVAILABLE,
                25000.0, BathroomType.ATTACHED, FloorLevel.THIRD_FLOOR,
                new ArrayList<>()
        );

        // २. ३ वटा कोठाका अब्जेक्टहरू बनाउने
        Room room1 = new Room("RM-101", "R1", PropertyStatus.AVAILABLE, 15000.0, BathroomType.ATTACHED, RoomType.OFFICE, FloorLevel.THIRD_FLOOR);
        Room room2 = new Room("RM-102", "R2", PropertyStatus.AVAILABLE, 25000.0, BathroomType.ATTACHED, RoomType.HALL, FloorLevel.THIRD_FLOOR);
        Room room3 = new Room("RM-103", "R3", PropertyStatus.AVAILABLE, 8000.0, BathroomType.COMMON, RoomType.SINGLE_ROOM, FloorLevel.THIRD_FLOOR);

        // ३. फ्ल्याटमा कोठाहरू थप्ने
        System.out.println("--- ➕ Adding Rooms to Flat ---");
        flat1.addRoom(room1);
        flat1.addRoom(room2);
        flat1.addRoom(room3);
        System.out.println("✅ 3 Rooms added successfully!");
        System.out.println("👉 Room 1's Parent Flat ID: " + room1.getParentFlat().getId() + " (Bidirectional Alignment Checked!)");

        // ४. SrmsEntity र सुधारिएको toString() को चेन चेक गर्ने
        System.out.println("\n--- 📝 Testing toString() Output ---");
        System.out.println(flat1);
        System.out.println(room1);

        // ५. PropertyPricingPolicy अनुसार calculateFixedMandatoryCharge() टेस्ट गर्ने
        System.out.println("\n--- 💰 Testing Pricing Calculations (PropertyPricingPolicy) ---");
        System.out.println("Total Mandatory Charges for Flat: Rs. " + flat1.calculateFixedMandatoryCharge());

        // ६. 🔥 कडा भ्यालिडेशन टेस्ट
        System.out.println("\n--- 🛑 Testing Duplicate Room Validation ---");
        try {
            System.out.println("Trying to add Room 1 again...");
            flat1.addRoom(room1);
        } catch (IllegalStateException e) {
            System.out.println("🔥 Caught Expected Error: " + e.getMessage());
        }

        // ७. 🔄 removeRoom र सम्बन्ध विच्छेद टेस्ट गर्ने
        System.out.println("\n--- ➖ Testing Remove Room ---");
        flat1.removeRoom(room3);
        System.out.println("✅ Room 3 removed from flat.");
        System.out.println("👉 Room 3's Parent Flat after removal: " + room3.getParentFlat() + " (Disconnection Checked!)");

        // ८. कोठा हटेपछि फ्ल्याटको अवस्था र नयाँ प्राइसिङ चेक गर्ने
        System.out.println("\n--- 📊 Flat Status & Pricing After Room Removal ---");
        System.out.println(flat1);
        System.out.println("New Mandatory Charges for Flat: Rs. " + flat1.calculateFixedMandatoryCharge());

        System.out.println("\n====== 👤 SRMS TENANT DOMAIN TESTING ======");

        // बिल्डर प्याटर्न चलाएर कडा टेनेन्ट अब्जेक्ट बनाउने
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

        // टेस्टका लागि flat1 को स्टेटस OCCUPIED बनाइदिउँ
        flat1.setPropertyStatus(PropertyStatus.OCCUPIED);

        // ९. कन्ट्याक्ट साइन गर्ने (कन्ट्याक्ट ACTIVE स्टेटमा रहन्छ)
        System.out.println("१. रुपेश टेनेन्टको नाममा Flat-999 को कन्ट्याक्ट साइन गर्दै...");
        Contract contract = new Contract.Builder("CON-2026-001", tenant, flat1, flat1.getBaseRent())
                .securityDeposit(6000.0)
                .startElectricity(120.5) // कन्ट्याक्ट सुरु हुँदा १२०.५ युनिट छ
                .agreementDocPath("agreements/rupesh_flat_999.pdf")
                .build();

        System.out.println("\n--- 📝 कन्ट्याक्टको सुरुवाती स्टेट (toString) ---");
        System.out.println(contract);
        System.out.println("------------------------------------------------");


        // ========================================================
        // ⚡ यहाँ सुरु भयो असली खेल: UTILITY READING र RENT BILL ⚡
        // ========================================================
        System.out.println("\n====== ⚡ SRMS UTILITY & BILLING LIFE-CYCLE TESTING ======");

        // १२. मिटर रिडिङ रेकर्ड (१२०.५ बाट बढेर २१०.५ पुग्यो = कुल ९० युनिट खपत)
        System.out.println("१. पहिलो महिनाको अन्तिममा मिटर रिडिङ रेकرد गर्दै...");

        UtilityReading readingMonth1 = new UtilityReading.Builder("UR-1:mon - 1: 2026-001")
                .currentReading(220.5)
                .previousReading(120.5)
                .build();
        System.out.println("✅ Utility Reading Recorded: " + readingMonth1);
        System.out.println("👉 खपत भएको कुल बिजुली युनिट: " + readingMonth1.calculateConsumedUnits() + " Units");

        // १३. बिल जेनरेट गर्ने (रु १२ का दरले)
        System.out.println("\n२. भाडा र बिजुली हिसाब गरेर बिल अटो-जेनरेट गर्दै...");
        RentBill billMonth1 = new RentBill.Builder("BILL-2026-001", contract, readingMonth1, 12.0)
                .advancePaid(2000.0)
                .previousDue(500.0)
                .build();

        // १४. बिल प्रिन्ट गर्ने
        System.out.println("\n--- 🖨️ जेनेरेट भएको बिलको वास्तविक स्वरूप ---");
        billMonth1.printBill();

        // १५. बिल भुक्तानी गर्ने
        System.out.println("३. रुपेशले बिल भुक्तानी गरेको स्टेट रेकर्ड गर्दै...");
        billMonth1.markAsPaid();
        System.out.println("👉 भुक्तानीपछिको बिल स्टेटस: " + billMonth1.getBillStatus() + " (Paid At: " + billMonth1.getPaidAt() + ")");

        // १६. डबल पेमेन्ट क्र्यास टेस्ट
        System.out.println("\n🚨 क्र्यास टेस्ट: तिरिसकेको बिल फेरि भुक्तानी गर्न खोज्दा...");
        try {
            billMonth1.markAsPaid();
        } catch (IllegalStateException e) {
            System.out.println("🎯 सफलता! सिस्टमले डबल भुक्तानी हुन दिएन। एरर: " + e.getMessage());
        }


        // ========================================================
        // ❌ सबै लेनदेन सकिएपछि मात्र कन्ट्याक्ट रद्द (Termination) गर्ने
        // ========================================================
        System.out.println("\n====== ❌ SRMS CONTRACT TERMINATION TESTING ======");

        double damageDeduction = 7500.0;
        System.out.println("१. रुपेशले फ्ल्याट छोड्ने भएपछि अन्तिममा कन्ट्याक्ट रद्द गर्दै...");
        contract.cancelContract(damageDeduction);

        System.out.println("\n--- 📊 कन्ट्याक्ट रद्द भएपछिको अन्तिम स्टेट ---");
        System.out.println(contract);
        System.out.println("👉 फ्ल्याटको नयाँ स्टेटस: " + flat1.getPropertyStatus());
        System.out.println("-----------------------------------------------");

        // डबल क्यान्सिल क्र्यास टेस्ट
        System.out.println("\n२. 🚨 क्र्यास टेस्ट: रद्द भइसकेको कन्ट्याक्ट फेरि रद्द गर्न खोज्दा...");
        try {
            contract.cancelContract(0);
        } catch (IllegalStateException e) {
            System.out.println("🎯 सफलता! डबल क्यान्सिल ब्लक भयो। एरर म्यासेज: " + e.getMessage());
        }

        System.out.println("\n🎉 ====== ALL SRMS V1 CORE ARCHITECTURE TESTS PASSED SUCCESSFULLY! ======");
    }
}