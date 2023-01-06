-- INCREASE & DECREASE LIKE
-- AFTER CREATE
CREATE FUNCTION increase_like_function()
RETURNS TRIGGER AS
$$
BEGIN
UPDATE video SET like_count = like_count+1 WHERE id = NEW.video_id and NEW.like_type = 'LIKE';
RETURN NEW;
END;
$$

LANGUAGE 'plpgsql';

CREATE TRIGGER increase_like
    AFTER INSERT ON video_like
    FOR EACH ROW
    EXECUTE PROCEDURE increase_like_function();

-- AFTER UPDATE
CREATE FUNCTION like_update_function()
RETURNS TRIGGER AS
$$
BEGIN
UPDATE video SET like_count = like_count+1 WHERE id = NEW.video_id and NEW.like_type = 'LIKE';
UPDATE video SET like_count = like_count-1 WHERE id = NEW.video_id and NEW.like_type = 'DISLIKE';
RETURN NEW;
END;
$$

LANGUAGE 'plpgsql';


CREATE TRIGGER update_like
    AFTER UPDATE ON video_like
    FOR EACH ROW
    EXECUTE PROCEDURE like_update_function();


-- AFTER DELETE
CREATE FUNCTION like_delete_function()
RETURNS TRIGGER AS
$$
BEGIN
UPDATE video SET like_count = like_count-1 WHERE id = NEW.video_id and NEW.like_type = 'LIKE';
RETURN NEW;
END;
$$

LANGUAGE 'plpgsql';


CREATE TRIGGER delete_like
    AFTER DELETE ON video_like
    FOR EACH ROW
    EXECUTE PROCEDURE like_delete_function();
