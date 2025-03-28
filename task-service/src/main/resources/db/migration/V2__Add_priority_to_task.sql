ALTER TABLE task
ADD COLUMN priority VARCHAR(20) DEFAULT 'MEDIUM';

ALTER TABLE task
ADD CONSTRAINT chk_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT'));

INSERT INTO task (title, description, completed, priority) VALUES
('Implement priority system', 'Add function to put priority on tasks', FALSE, 'HIGH');