package com.github.malahor.pastemein;

import com.github.malahor.pastemein.domain.Paste;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface PasteRepository extends CrudRepository<Paste, UUID> {}
