// Script to populate parking lot with 650 vehicles
const API_BASE = 'http://localhost:8080/parkingLot';

async function parkVehicle(vehicleType, regNo) {
    try {
        const response = await fetch(`${API_BASE}/doParking`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                vehicleType: vehicleType,
                registrationNo: regNo
            })
        });
        
        if (response.ok) {
            const data = await response.json();
            console.log(`✓ ${regNo} (${vehicleType}) - ${data.basementNo}`);
            return true;
        } else {
            console.log(`✗ ${regNo} - Failed to park`);
            return false;
        }
    } catch (error) {
        console.log(`✗ ${regNo} - Error: ${error.message}`);
        return false;
    }
}

async function populateParkingLot() {
    console.log('Starting to populate parking lot with 650 vehicles...');
    console.log('This will take some time...\n');
    
    let successCount = 0;
    
    // Add 200 bikes (2 floors × 100 each for testing)
    for (let i = 1; i <= 200; i++) {
        const regNo = `KA${String(i).padStart(2, '0')}BK${String(i).padStart(4, '0')}`;
        const success = await parkVehicle('BIKE', regNo);
        if (success) successCount++;
        
        // Small delay to avoid overwhelming the server
        await new Promise(resolve => setTimeout(resolve, 50));
    }
    
    // Add 450 cars (5 floors × 90 each for testing)
    for (let i = 1; i <= 450; i++) {
        const regNo = `KA${String(i).padStart(2, '0')}CR${String(i).padStart(4, '0')}`;
        const success = await parkVehicle('CAR', regNo);
        if (success) successCount++;
        
        // Small delay to avoid overwhelming the server
        await new Promise(resolve => setTimeout(resolve, 50));
    }
    
    console.log(`\nCompleted! Successfully parked ${successCount} vehicles.`);
}

// Run the script
populateParkingLot();
