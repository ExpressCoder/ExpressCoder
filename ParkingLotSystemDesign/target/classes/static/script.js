const API_BASE = '/parkingLot';

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    loadStats();
    loadFloors();
    loadVehicles();
    initializeVisualization();
    
    // Set up form listeners
    document.getElementById('parkingForm').addEventListener('submit', handleParking);
    document.getElementById('exitForm').addEventListener('submit', handleExit);
    
    // Auto-refresh every 10 seconds
    setInterval(() => {
        loadStats();
        loadFloors();
        loadVehicles();
        updateVisualizationSpots();
    }, 10000);
});

// Load statistics
async function loadStats() {
    try {
        const totalResponse = await fetch(`${API_BASE}/availableLots`);
        const totalData = await totalResponse.json();
        document.getElementById('totalAvailable').textContent = totalData;
        
        const bikeResponse = await fetch(`${API_BASE}/remainingLots?vehicleType=BIKE`);
        const bikeData = await bikeResponse.json();
        document.getElementById('bikeAvailable').textContent = bikeData;
        
        const carResponse = await fetch(`${API_BASE}/remainingLots?vehicleType=CAR`);
        const carData = await carResponse.json();
        document.getElementById('carAvailable').textContent = carData;
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

// Load floor information
async function loadFloors() {
    try {
        const floorsGrid = document.getElementById('floorsGrid');
        floorsGrid.innerHTML = '';
        
        const floorConfig = [
            { number: 1, type: 'BIKE', label: 'Bike' },
            { number: 2, type: 'BIKE', label: 'Bike' },
            { number: 3, type: 'CAR', label: 'Car' },
            { number: 4, type: 'CAR', label: 'Car' },
            { number: 5, type: 'CAR', label: 'Car' },
            { number: 6, type: 'CAR', label: 'Car' },
            { number: 7, type: 'CAR', label: 'Car' }
        ];
        
        for (const floor of floorConfig) {
            const capacityResponse = await fetch(`${API_BASE}/floor/${floor.number}/availableCapacity`);
            const capacityData = await capacityResponse.json();
            
            const occupancyResponse = await fetch(`${API_BASE}/floor/${floor.number}/currentOccupancy`);
            const occupancyData = await occupancyResponse.json();
            
            const floorCard = document.createElement('div');
            floorCard.className = 'floor-card';
            floorCard.innerHTML = `
                <div class="floor-number">Floor ${floor.number}</div>
                <div class="floor-type">${floor.label} Parking</div>
                <div class="floor-capacity">${capacityData} spots</div>
                <div class="floor-occupancy">${occupancyData} occupied</div>
            `;
            floorsGrid.appendChild(floorCard);
        }
    } catch (error) {
        console.error('Error loading floors:', error);
    }
}

// Load parked vehicles
async function loadVehicles() {
    try {
        const response = await fetch(`${API_BASE}/listOfVehiclesInLot`);
        const vehicles = await response.json();
        
        const tableBody = document.getElementById('vehiclesTableBody');
        tableBody.innerHTML = '';
        
        if (vehicles.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="4" class="empty-state">No vehicles parked</td></tr>';
            return;
        }
        
        vehicles.forEach(vehicle => {
            const row = document.createElement('tr');
            const entryTime = new Date(vehicle.enteredDateTime).toLocaleString();
            const floorNumber = vehicle.floor ? vehicle.floor.floorNumber : 'N/A';
            
            row.innerHTML = `
                <td>${vehicle.registrationNo}</td>
                <td>${vehicle.vehicleType}</td>
                <td>Floor ${floorNumber}</td>
                <td>${entryTime}</td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading vehicles:', error);
    }
}

// Handle parking form submission
async function handleParking(event) {
    event.preventDefault();
    
    const vehicleType = document.getElementById('vehicleType').value;
    const registrationNo = document.getElementById('registrationNo').value.toUpperCase();
    
    if (!vehicleType || !registrationNo) {
        showMessage('Please fill in all fields', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/doParking`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                vehicleType: vehicleType,
                registrationNo: registrationNo
            })
        });
        
        if (response.ok) {
            const data = await response.json();
            const floorNumber = data.floor ? data.floor.floorNumber : null;
            animateCarEntering(floorNumber);
            showMessage(`Vehicle parked successfully! ${data.basementNo}. Token: ${data.tokenId}`, 'success');
            document.getElementById('parkingForm').reset();
            loadStats();
            loadFloors();
            loadVehicles();
            updateVisualizationSpots();
        } else {
            const errorData = await response.json();
            showMessage(errorData.message || 'Failed to park vehicle', 'error');
        }
    } catch (error) {
        console.error('Error parking vehicle:', error);
        showMessage('Error parking vehicle. Please try again.', 'error');
    }
}

// Handle exit form submission
async function handleExit(event) {
    event.preventDefault();
    
    const registrationNo = document.getElementById('exitRegistrationNo').value.toUpperCase();
    
    if (!registrationNo) {
        showMessage('Please enter registration number', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/exitParking/${registrationNo}`);
        
        if (response.ok) {
            const data = await response.json();
            const price = data.price ? `₹${data.price}` : 'N/A';
            animateCarExiting();
            showMessage(`Vehicle exited successfully! Parking fee: ${price}`, 'success');
            document.getElementById('exitForm').reset();
            loadStats();
            loadFloors();
            loadVehicles();
            updateVisualizationSpots();
        } else {
            const errorData = await response.json();
            showMessage(errorData.message || 'Failed to exit vehicle', 'error');
        }
    } catch (error) {
        console.error('Error exiting vehicle:', error);
        showMessage('Error exiting vehicle. Please try again.', 'error');
    }
}

// Refresh vehicles list
function refreshVehicles() {
    loadVehicles();
    showMessage('Vehicle list refreshed', 'info');
}

// Show message to user
function showMessage(message, type) {
    const messageContainer = document.getElementById('message');
    messageContainer.textContent = message;
    messageContainer.className = `message ${type}`;
    
    setTimeout(() => {
        messageContainer.className = 'message';
        messageContainer.textContent = '';
    }, 5000);
}

// Visualization functions
function initializeVisualization() {
    const floorConfig = [
        { number: 1, maxSpots: 200 },
        { number: 2, maxSpots: 200 },
        { number: 3, maxSpots: 50 },
        { number: 4, maxSpots: 50 },
        { number: 5, maxSpots: 50 },
        { number: 6, maxSpots: 50 },
        { number: 7, maxSpots: 50 }
    ];
    
    floorConfig.forEach(floor => {
        const floorElement = document.querySelector(`.floor-${floor.number} .parking-spots`);
        if (floorElement) {
            floorElement.innerHTML = '';
            // Create visual spots (limited to 20 for display purposes)
            const displaySpots = Math.min(floor.maxSpots, 20);
            for (let i = 0; i < displaySpots; i++) {
                const spot = document.createElement('div');
                spot.className = 'spot';
                spot.dataset.floor = floor.number;
                floorElement.appendChild(spot);
            }
        }
    });
    
    updateVisualizationSpots();
}

async function updateVisualizationSpots() {
    try {
        const floorConfig = [
            { number: 1, maxSpots: 200 },
            { number: 2, maxSpots: 200 },
            { number: 3, maxSpots: 50 },
            { number: 4, maxSpots: 50 },
            { number: 5, maxSpots: 50 },
            { number: 6, maxSpots: 50 },
            { number: 7, maxSpots: 50 }
        ];
        
        for (const floor of floorConfig) {
            const occupancyResponse = await fetch(`${API_BASE}/floor/${floor.number}/currentOccupancy`);
            const occupancyData = await occupancyResponse.json();
            
            const floorElement = document.querySelector(`.floor-${floor.number} .parking-spots`);
            if (floorElement) {
                const spots = floorElement.querySelectorAll('.spot');
                const displaySpots = spots.length;
                const occupancyRatio = occupancyData / floor.maxSpots;
                const occupiedDisplaySpots = Math.floor(displaySpots * occupancyRatio);
                
                spots.forEach((spot, index) => {
                    if (index < occupiedDisplaySpots) {
                        spot.classList.add('occupied');
                    } else {
                        spot.classList.remove('occupied');
                    }
                });
            }
        }
    } catch (error) {
        console.error('Error updating visualization:', error);
    }
}

function animateCarEntering(floorNumber) {
    const car = document.getElementById('animatedCar');
    const status = document.getElementById('animationStatus');
    
    // Reset car position
    car.classList.remove('exiting');
    car.style.left = '-100px';
    car.style.opacity = '1';
    
    status.textContent = `Vehicle entering... going to Floor ${floorNumber}`;
    
    // Trigger animation
    setTimeout(() => {
        car.classList.add('entering');
    }, 100);
    
    // Clear status after animation
    setTimeout(() => {
        car.classList.remove('entering');
        car.style.left = '-100px';
        status.textContent = '';
    }, 2500);
}

function animateCarExiting() {
    const car = document.getElementById('animatedCar');
    const status = document.getElementById('animationStatus');
    
    // Reset car position to center
    car.classList.remove('entering');
    car.style.left = '50%';
    car.style.opacity = '1';
    
    status.textContent = 'Vehicle exiting parking lot...';
    
    // Trigger animation
    setTimeout(() => {
        car.classList.add('exiting');
    }, 100);
    
    // Clear status after animation
    setTimeout(() => {
        car.classList.remove('exiting');
        car.style.left = '-100px';
        status.textContent = '';
    }, 2500);
}
