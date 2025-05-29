const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');

const app = express();
const port = 3001;

app.use(cors());

// MySQL connection
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: 'Vrushali@#123',
    database: 'realestate'
});

db.connect(err => {
    if (err) {
        console.error('Database connection failed:', err);
        process.exit(1);
    }
    console.log('Connected to MySQL database');
});

// Helper to sanitize price input
const sanitizePrice = (val) => {
    if (!val) return null;
    const num = parseFloat(val.toString().replace(/[^0-9.]/g, ''));
    return isNaN(num) ? null : num;
};

// ==============================
// GET /api/properties
// ==============================
app.get('/api/properties', (req, res) => {
    let query = 'SELECT * FROM chatbot_database WHERE 1=1';
    const queryParams = [];

    const { city, bhk, acquisitionType, status, furnishing, priceMin, priceMax, property_name, sub_area } = req.query;

    if (!property_name && !sub_area && !city && !bhk && !acquisitionType && !status && !furnishing && !(priceMin && priceMax)) {
        return res.json([]);
    }

    // Build query dynamically
    if (city) {
        query += ' AND city = ?';
        queryParams.push(city);
    }
    if (sub_area) {
        query += ' AND sub_area = ?';
        queryParams.push(sub_area);
    }
    if (bhk) {
        query += ' AND bhk = ?';
        queryParams.push(bhk);
    }
    if (acquisitionType) {
        query += ' AND acquisitionType = ?';
        queryParams.push(acquisitionType);
    }
    if (status) {
        query += ' AND status = ?';
        queryParams.push(status);
    }
    if (furnishing) {
        query += ' AND furnishing = ?';
        queryParams.push(furnishing);
    }


    console.log('Running Query:', query);
    console.log('With Parameters:', queryParams);

    db.query(query, queryParams, (err, results) => {
        if (err) {
            console.error('Error fetching properties:', err);
            res.status(500).send('Error fetching data');
            return;
        }

        // Save each match with actual price to user_searches
        results.forEach(property => {
            db.query(
                `INSERT INTO user_searches 
                (city,  sub_area, bhk, acquisitionType, status, furnishing, actual_price)
                VALUES (?, ?, ?, ?, ?, ?, ? )`,
                [
                    city || null,
                    sub_area || null,
                    bhk || null,
                    acquisitionType || null,
                    status || null,
                    furnishing || null,
                    property.price || null
                ],
                (err) => {
                    if (err) {
                        console.error('Error saving user search:', err);
                    }
                }
            );
        });

        res.json(results);
    });
});

// ==============================
// GET /api/property/:id
// ==============================
app.get('/api/property/:id', (req, res) => {
    const { id } = req.params;
    const query = 'SELECT * FROM chatbot_database WHERE id = ?';

    db.query(query, [id], (err, results) => {
        if (err) {
            console.error('Error fetching property:', err);
            res.status(500).send('Error fetching data');
        } else if (results.length === 0) {
            res.status(404).send('Property not found');
        } else {
            res.json(results[0]);
        }
    });
});

// ==============================
// GET /api/agent/:id
// ==============================
app.get('/api/agent/:id', (req, res) => {
    const { id } = req.params;
    const query = `
        SELECT 
            agent_name,
            agent_phone,
            agent_experience,
            agent_company,
            fake_email
        FROM chatbot_database
        WHERE id = ?
    `;

    db.query(query, [id], (err, results) => {
        if (err) {
            console.error('Error fetching agent info:', err);
            res.status(500).send('Error fetching agent info');
        } else if (results.length === 0) {
            res.status(404).send('Agent not found');
        } else {
            res.json(results[0]);
        }
    });
});

// ==============================
// Start Server
// ==============================
app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});
